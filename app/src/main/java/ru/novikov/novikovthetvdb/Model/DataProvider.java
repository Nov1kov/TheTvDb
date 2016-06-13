package ru.novikov.novikovthetvdb.Model;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseSuccessful;

/**
 *
 */
public class DataProvider implements RestRepository.SaverToken {

    public final static int SERIES_PORTION_COUNT = 20;

    private RestRepository remoteRepository;
    private PreferencesRepository preferencesRepository;
    private MemoryRepository memoryRepository;
    private List<DataProviderSubscriber> subscribers;

    public DataProvider(RestRepository repository, PreferencesRepository preferencesRepository){
        this.remoteRepository = repository;
        memoryRepository = new MemoryRepository();
        subscribers = new ArrayList<>();
        this.preferencesRepository = preferencesRepository;
        remoteRepository.setAuthToken(preferencesRepository.getKeyTvdbToken());
        remoteRepository.setSaverInfoListener(this);
    }

    public void setSubscriber(DataProviderSubscriber subscriber){
        if (!subscribers.contains(subscriber))
            subscribers.add(subscriber);
    }

    public void deleteSubscriber(DataProviderSubscriber subscriber){
        subscribers.remove(subscriber);
    }

    public void getSeriesDetail(final long seriesId){
        Series memObj = memoryRepository.getSeriesDetail(seriesId);
        if (memObj == null){
            remoteRepository.getSeries(seriesId, new ResponseSuccessful<Series>() {
                @Override
                public void response(Series body) {
                    memoryRepository.setSeries(body);
                    for (DataProviderSubscriber subscriber : subscribers){
                        subscriber.receivedSeries(body);
                    }
                }
            });
        } else {
            for (DataProviderSubscriber subscriber : subscribers)
                subscriber.receivedSeries(memObj);
        }

    }

    public void getFullSeriesList(int from){
        remoteRepository.getLastSeriesList(from, SERIES_PORTION_COUNT, new ResponseSuccessful<List<Series>>() {
            @Override
            public void response(List<Series> body) {
                for (DataProviderSubscriber subscriber : subscribers)
                    subscriber.receivedSeriesList(body);
            }
        });
    }

    public void getEpisodes(final long seriesId, final String page){
        remoteRepository.getEpisodes(seriesId, page, new ResponseSuccessful<List<Episode>>() {
            @Override
            public void response(List<Episode> body) {
                for (DataProviderSubscriber subscriber : subscribers)
                    subscriber.receiveEpisodes(body);
            }
        });
    }

    public void getActors(final long seriesId){
        remoteRepository.getActors(seriesId, new ResponseSuccessful<List<Actor>>() {
            @Override
            public void response(List<Actor> body) {
                for (DataProviderSubscriber subscriber : subscribers)
                    subscriber.receiveActors(body);
            }
        });
    }

    @Override
    public void onSaveToken(String token) {
        if (preferencesRepository != null)
            preferencesRepository.saveTvDbToken(token);
    }
}
