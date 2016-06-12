package ru.novikov.novikovthetvdb.Model;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.AfterLogin;
import ru.novikov.novikovthetvdb.Model.Rest.ApiClient;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseSuccessful;

/*
    remote repository with auto login
*/
public class RestRepository {

    private ApiClient client;

    public RestRepository(){
        client = new ApiClient("", "");
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
                });
            }
        });
    }

    public void getSeriesLastWeek(final ResponseSuccessful<List<SeriesData>> callback) {

        client.CheckAuth(new AfterLogin() {
            @Override
            public void loginSucses() {
                int fromTime = (int) (System.currentTimeMillis() / 1000);
                int toTime = (int) (System.currentTimeMillis() / 1000) - 5000; //
                client.GetSeriesRecent(String.valueOf(toTime), null, new ResponseSuccessful<SeriesRecentResponse>() {
                    @Override
                    public void response(SeriesRecentResponse body) {
                        callback.response(body.data);
                    }
                });
            }
        });
    }

}
