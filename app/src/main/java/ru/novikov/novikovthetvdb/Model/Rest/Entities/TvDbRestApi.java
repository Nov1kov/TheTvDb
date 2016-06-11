package ru.novikov.novikovthetvdb.Model.Rest.Entities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Requests.Login;
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
}
