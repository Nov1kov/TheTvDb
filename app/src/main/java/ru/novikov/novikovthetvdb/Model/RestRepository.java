package ru.novikov.novikovthetvdb.Model;

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

    private ApiClient client;

    public RestRepository(){
        client = new ApiClient("", "");
    }

    public void getEpisodes(final long seriesId, final String page, final ResponseSuccessful<List<Episode>> callback){
        client.GetEpisodes(seriesId, page, new ResponseSuccessful<Episodes>() {
            @Override
            public void response(Episodes body) {
                callback.response(body.data);
            }
        }, null);
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
        client.CheckAuth(new AfterLogin() {
            @Override
            public void loginSucses() {
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

        client.CheckAuth(new AfterLogin() {
            @Override
            public void loginSucses() {
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

}
