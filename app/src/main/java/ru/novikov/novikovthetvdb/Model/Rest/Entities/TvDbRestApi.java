package ru.novikov.novikovthetvdb.Model.Rest.Entities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Requests.Login;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actors;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episodes;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.EpisodesSummary;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Token;


public interface TvDbRestApi {

    @POST("/login")
    Call<Token> login(@Body Login login);

    @GET("/updated/query")
    Call<SeriesRecentResponse> seriesRecent(@Query("fromTime") String fromTime, @Query("toTime") String toTime);

    @GET("/series/{id}")
    Call<SeriesResponse> getSeries(@Path("id") long id);

    /*
    Returns a summary of the episodes and seasons available for the series.
    Note: Season "0" is for all episodes that are considered to be specials.
     */
    @GET("/series/{id}/episodes/summary")
    Call<EpisodesSummary> episodesSummary(@Path("id") long seriesId);

    /*
    All episodes for a given series. Paginated with 100 results per page.
    Page of results to fetch. Defaults to page 1 if not provided.
     */
    @GET("/series/{id}/episodes")
    Call<Episodes> episodes(@Path("id") long seriesId, @Query("page") String page);

    /*
    Returns actors for the given series id
     */
    @GET("/series/{id}/actors")
    Call<Actors> actors(@Path("id") long seriesId);
}
