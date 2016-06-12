package ru.novikov.novikovthetvdb.Model;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseSuccessful;

/**
 *
 */
public class DataProvider {

    public final static int SERIES_PORTION_COUNT = 20;

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

    public void getSeriesDetailCallBack(final long seriesId, ResponseSuccessful<Series> callback){
        remoteRepository.getSeries(seriesId, callback);
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
        } else {
            if (subscriber != null)
                subscriber.receivedSeries(memObj);
        }

    }

    public void getFullSeriesList(int from){
        remoteRepository.getLastSeriesList(from, SERIES_PORTION_COUNT, new ResponseSuccessful<List<Series>>() {
            @Override
            public void response(List<Series> body) {
                if (subscriber != null)
                    subscriber.receivedSeriesList(body);
            }
        });
    }

/*    public void getSeriesList(){
        remoteRepository.getSeriesLastWeek(new ResponseSuccessful<List<SeriesData>>() {
            @Override
            public void response(List<SeriesData> body) {
                if (subscriber != null)
                    subscriber.receivedSeriesList(body);
            }
        });
    }*/
}
