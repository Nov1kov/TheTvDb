package ru.novikov.novikovthetvdb.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.AfterLogin;
import ru.novikov.novikovthetvdb.Model.Rest.ApiClient;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actors;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episodes;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseFail;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseSuccessful;

/*
    remote repository with auto login
    convert response json object to entity
*/
public class RestRepository {

    private static final String TVDB_APIKEY = "3349F1D32F314DE9";
    private static final String TAG = "RestRepository";

    private ApiClient client;
    private SaverToken saverInfo;

    public RestRepository(){
        client = new ApiClient("", "", TVDB_APIKEY);
    }

    public void setSaverInfoListener(SaverToken saverInfo){
        this.saverInfo = saverInfo;
    }

    public void setAuthToken(String authToken){
        if (authToken != null)
            Log.i(TAG, "set new token");
        client.setAuthToken(authToken);
    }

    public void getEpisodes(final long seriesId, final String page, final ResponseSuccessful<List<Episode>> callback){
        client.GetEpisodes(seriesId, page, new ResponseSuccessful<Episodes>() {
            @Override
            public void response(Episodes body) {
                callback.response(body.data);
            }
        }, null);
    }

    private boolean CheckAuth(final AfterLogin afterLogin){
        if (client.validAuthToken()){
            afterLogin.loginSuccessfully();
            return true;
        } else {
            client.Authentication(new ResponseSuccessful<String>() {
                @Override
                public void response(String token) {
                    if (saverInfo != null)
                        saverInfo.onSaveToken(token);
                    client.setAuthToken(token);
                    afterLogin.loginSuccessfully();
                }
            }, null);
            return false;
        }
    }

    public void getActors(final long seriesId, final ResponseSuccessful<List<Actor>> callback){
        client.GetActors(seriesId, new ResponseSuccessful<Actors>() {
            @Override
            public void response(Actors body) {
                callback.response(body.data);
            }
        }, null);
    }

    public void getSeries(final long seriesId, final ResponseSuccessful<Series> callback){
        CheckAuth(new AfterLogin() {
            @Override
            public void loginSuccessfully() {
                client.GetSeriesInfo(seriesId, new ResponseSuccessful<SeriesResponse>() {
                    @Override
                    public void response(SeriesResponse body) {
                        callback.response(body.data);
                    }
                }, null);
            }
        });
    }

    public void getLastSeriesList(final int from, final int seriesCount, final ResponseSuccessful<List<Series>> callback){
        final ArrayList<Series> series = new ArrayList<>();
        getSeriesLastWeek(new ResponseSuccessful<List<SeriesData>>() {
            @Override
            public void response(final List<SeriesData> seriesDatas) {
                for (int i = from; (i < from + seriesCount) && (i < seriesDatas.size()); i++) {

                    client.GetSeriesInfo(seriesDatas.get(i).id, new ResponseSuccessful<SeriesResponse>() {
                        @Override
                        public void response(SeriesResponse body) {
                            series.add(body.data);
                            if ((series.size() == seriesCount) || (series.size() == seriesDatas.size())){
                                callback.response(series);
                            }
                        }
                    }, new ResponseFail() {
                        @Override
                        public void onFail(String message) {
                            series.add(new Series());
                            if ((series.size() == seriesCount) || (series.size() == seriesDatas.size())){
                                callback.response(series);
                            }
                        }
                    });
                }
            }
        });
    }

    public void getSeriesLastWeek(final ResponseSuccessful<List<SeriesData>> callback) {

        CheckAuth(new AfterLogin() {
            @Override
            public void loginSuccessfully() {
                int fromTime = (int) (System.currentTimeMillis() / 1000);
                int toTime = (int) (System.currentTimeMillis() / 1000) - 50000; //
                client.GetSeriesRecent(String.valueOf(toTime), null, new ResponseSuccessful<SeriesRecentResponse>() {
                    @Override
                    public void response(SeriesRecentResponse body) {
                        callback.response(body.data);
                    }
                }, null);
            }
        });
    }

    /*
    for save token to shared pref
     */
    public interface SaverToken {
        void onSaveToken(String token);
    }

}
