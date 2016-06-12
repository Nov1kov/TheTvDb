package ru.novikov.novikovthetvdb.Model;

import java.util.HashMap;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;

/**
 * Memory save data
 */
public class MemoryRepository {

    private HashMap<Long, Series> seriesMap = new HashMap<>();

    public Series getSeriesDetail(final long seriesId){
        return seriesMap.get(seriesId);
    }

    public void setSeries(Series series){
        seriesMap.put(series.id, series);
    }

}

