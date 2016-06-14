package ru.novikov.novikovthetvdb.Model;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;

/**
 *
 */
public interface DataProviderSubscriber {

    void receivedSeriesList(List<Series> seriesDataList);

    void receivedSeries(Series series);

    void receiveActors(List<Actor> actorList);

    void receiveEpisodes(List<Episode> episodesList);

    void receivedFail(String msg);

}
