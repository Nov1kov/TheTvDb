package ru.novikov.novikovthetvdb.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import ru.novikov.novikovthetvdb.Model.Rest.AfterLogin;
import ru.novikov.novikovthetvdb.Model.Rest.ApiClient;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseSuccessful;

public class RestRepository implements IRepository {
    private ApiClient client;

    public RestRepository(){

        client = new ApiClient("", "");
    }


    public void getSeries(final long seriesId){
        client.GetHeadSeries(seriesId, new ResponseSuccessful<SeriesResponse>() {
            @Override
            public void response(SeriesResponse body) {
                Log.i("frefre", body.toString());
            }
        });
    }

    @Override
    public List<ShowItemList> getShowsLastWeek() {

        client.CheckAuth(new AfterLogin() {
            @Override
            public void loginSucses() {
                int time = (int) (System.currentTimeMillis() / 1000) - 605000;
                client.GetSeriesRecent(String.valueOf(time), null, new ResponseSuccessful<SeriesRecentResponse>() {
                    @Override
                    public void response(SeriesRecentResponse body) {
                        for (int i = 0; i < 10; i++){
                            getSeries(body.data.get(i).id);

                        }

                    }
                });
            }
        });

        ArrayList<ShowItemList> mockList = new ArrayList<>();
        mockList.add(getItemListObj());
        mockList.add(getItemListObj());
        mockList.add(getItemListObj());
        mockList.add(getItemListObj());
        mockList.add(getItemListObj());
        mockList.add(getItemListObj());
        mockList.add(getItemListObj());
        return mockList;
    }

    static private ShowItemList getItemListObj(){
        ShowItemList obj = new ShowItemList();
        obj.id = 0;
        obj.overview = "frefreferfrefre";
        obj.seriesName = "Sherlock";
        return obj;
    }
}
