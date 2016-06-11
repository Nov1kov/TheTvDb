package ru.novikov.novikovthetvdb.Model;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;

/**
 *
 */
public class DataProvider {

    private IRepository repository;

    public DataProvider(IRepository repository){
        this.repository = repository;
    }

    public List<ShowItemList> getShows(){

        return repository.getShowsLastWeek();
    }

    public SeriesResponse.Series getSeries(String string) {

        return null;
    }
}
