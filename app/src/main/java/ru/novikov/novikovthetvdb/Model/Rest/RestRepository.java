package ru.novikov.novikovthetvdb.Model.Rest;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actors;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episodes;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;

/*
    remote repository with auto login
    convert response json object to entity
*/
public class RestRepository {

    private static final String TVDB_APIKEY = "3349F1D32F314DE9";
    private static final String TAG = "RestRepository";
    private static final int TIME_NEW_SERIES_AWAY = 50000; //ToDo взято на обум чтобы выдавало 250-300 элементов

    private ApiClient client;
    private SaverToken saverInfo;
    private ResponseFail responseFail;

    public RestRepository(){
        client = new ApiClient("", "", TVDB_APIKEY);
    }

    public void setFailListener(ResponseFail responseFail){
        this.responseFail = responseFail;
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
        }, responseFail);
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
        }, responseFail);
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
                }, responseFail);
            }
        });
    }

    public void getLastSeriesList(final int from, final int seriesCount,
                                  final ResponseSuccessful<List<Series>> callback){
        final ArrayList<Series> series = new ArrayList<>();
        getSeriesLastWeek(new ResponseSuccessful<List<SeriesData>>() {
            @Override
            public void response(final List<SeriesData> seriesDates) {
                for (int i = from; (i < from + seriesCount) && (i < seriesDates.size()); i++) {

                    client.GetSeriesInfo(seriesDates.get(i).id, new ResponseSuccessful<SeriesResponse>() {
                        @Override
                        public void response(SeriesResponse body) {
                            series.add(body.data);
                            if ((series.size() == seriesCount) || (series.size() == seriesDates.size())){
                                callback.response(series);
                            }
                        }
                    }, new ResponseFail() {
                        @Override
                        public void onFail(String message) {
                            series.add(new Series());
                            if ((series.size() == seriesCount) || (series.size() == seriesDates.size())){
                                callback.response(series);
                            }
                        }
                    });
                }
            }
        });
    }

    private void getSeriesLastWeek(final ResponseSuccessful<List<SeriesData>> callback) {

        CheckAuth(new AfterLogin() {
            @Override
            public void loginSuccessfully() {
                int fromTime = (int) (System.currentTimeMillis() / 1000);
                int toTime = (int) (System.currentTimeMillis() / 1000) - TIME_NEW_SERIES_AWAY; //
                client.GetSeriesRecent(String.valueOf(toTime), null, new ResponseSuccessful<SeriesRecentResponse>() {
                    @Override
                    public void response(SeriesRecentResponse body) {
                        callback.response(body.data);
                    }
                }, responseFail);
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
