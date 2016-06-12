package ru.novikov.novikovthetvdb.View;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;
import ru.novikov.novikovthetvdb.ShowsApp;

/**
 *
 */
public interface SeriesListView {

    void showProgressBar();

    void showSeriesList(List<SeriesData> seriesList);

    void showConnectionError();

    ShowsApp getApp();

}
