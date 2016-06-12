package ru.novikov.novikovthetvdb.Model;

import android.database.Observable;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseSuccessful;

/**
 *
 */
public class DataProvider {

    private RestRepository remoteRepository;
    private MemoryRepository memoryRepository;
    private DataProviderSubscriber subscriber;

    public DataProvider(RestRepository repository){
        this.remoteRepository = repository;
        memoryRepository = new MemoryRepository();

    }

    public void setSubscriber(DataProviderSubscriber subscriber){
        this.subscriber = subscriber;
    }

    public void deleteSubscriber(){
        this.subscriber = null;
    }

    public void getSeriesDetail(final long seriesId){
        Series memObj = memoryRepository.getSeriesDetail(seriesId);
        if (memObj == null){
            remoteRepository.getSeries(seriesId, new ResponseSuccessful<Series>() {
                @Override
                public void response(Series body) {
                    memoryRepository.setSeries(body);
                    if (subscriber != null)
                        subscriber.receivedSeries(body);
                }
            });

        }else {
            if (subscriber != null)
                subscriber.receivedSeries(memObj);
        }

    }

    public void getLastSeriesList(){
        remoteRepository.getSeriesLastWeek(new ResponseSuccessful<List<SeriesData>>() {
            @Override
            public void response(List<SeriesData> body) {
                for (int i = 0; i < 20; i++) {
                    getSeriesDetail(body.get(i).id);
                }
            }
        });
    }

    public void getSeriesList(){
        remoteRepository.getSeriesLastWeek(new ResponseSuccessful<List<SeriesData>>() {
            @Override
            public void response(List<SeriesData> body) {
                if (subscriber != null)
                    subscriber.receivedSeriesList(body);
            }
        });
    }
}
