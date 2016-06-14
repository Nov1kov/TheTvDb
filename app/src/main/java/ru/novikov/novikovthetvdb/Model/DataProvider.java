package ru.novikov.novikovthetvdb.Model;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.novikovthetvdb.Model.DataBase.DataBaseRepository;
import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.FavoriteItem;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.ResponseSuccessful;
import ru.novikov.novikovthetvdb.Model.Rest.RestRepository;
import ru.novikov.novikovthetvdb.Model.SharedPreferences.PreferencesRepository;

/**
 *
 */
public class DataProvider implements RestRepository.SaverToken {

    public final static int SERIES_PORTION_COUNT = 20;

    private RestRepository remoteRepository;

    private PreferencesRepository preferencesRepository;
    private DataBaseRepository dataBaseRepository;

    private List<DataProviderSubscriber> subscribers;

    public DataProvider(RestRepository repository, PreferencesRepository preferencesRepository, DataBaseRepository dataBaseRepository){
        this.remoteRepository = repository;
        this.dataBaseRepository = dataBaseRepository;
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
        remoteRepository.getSeries(seriesId, new ResponseSuccessful<Series>() {
            @Override
            public void response(Series body) {
                for (DataProviderSubscriber subscriber : subscribers){
                    subscriber.receivedSeries(body);
                }
            }
        });
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

    /*
    Принимает токен и сохраняет в sharedPref
     */
    @Override
    public void onSaveToken(String token) {
        if (preferencesRepository != null)
            preferencesRepository.saveTvDbToken(token);
    }

    public void updateGoogleName(String name){
        preferencesRepository.saveGoogleName(name);
    }

    public void saveFavoriteSeries(Series series){
        //TODO move to adapter dataProvider -> adapter -> dataBaseRepository
        FavoriteItem favoriteItem =  new FavoriteItem();
        favoriteItem.setSeriesId(series.id);
        favoriteItem.setGenre(series.genre.toString());
        favoriteItem.setOwner(preferencesRepository.getGoogleName());
        favoriteItem.setSeriesName(series.seriesName);
        dataBaseRepository.addFavoriteItem(favoriteItem);
    }

    public List<FavoriteItem> getFavoritesList() {
        return dataBaseRepository.getAllFavoritesItems(preferencesRepository.getGoogleName());
    }
}
