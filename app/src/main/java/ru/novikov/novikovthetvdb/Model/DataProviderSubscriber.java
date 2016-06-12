package ru.novikov.novikovthetvdb.Model;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;

/**
 *
 */
public interface DataProviderSubscriber {

    void receivedSeriesList(List<SeriesData> seriesDataList);

    void receivedSeries(Series series);

}
