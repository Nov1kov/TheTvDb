package ru.novikov.novikovthetvdb.Model.Rest;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actors;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episodes;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.TvDbRestApi;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Requests.Login;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Token;


public class ApiClient {

    private static final String LOG_TAG = "ApiClient";
    private static final String TVDB_BASE_URL = "https://api.thetvdb.com";
    public static final String TVDB_IMAGES_URL = "http://thetvdb.com/banners/";
    private static final String TVDB_VERSION = "2";
    public static final Object TAG_CALL = new Object();

    private final String pass;
    private final String login;
    private final String apiKey;

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private Retrofit.Builder builder;
    TvDbRestApi tvDbAuthApi;

    private String authToken = null;

    public ApiClient(final String login, final String pass, final String apiKey) {
        builder = new Retrofit.Builder()
                        .baseUrl(TVDB_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        tvDbAuthApi = createService(TvDbRestApi.class);
        this.login = login;
        this.pass = pass;
        this.apiKey = apiKey;
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
        tvDbAuthApi = createService(TvDbRestApi.class);
    }

    public OkHttpClient getHttpClient(){
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        //.header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + authToken)
                        .header("Accept", "application/json") //vnd.thetvdb.v" + TVDB_VERSION
                        .method(original.method(), original.body())
                        .tag(TAG_CALL);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    private  <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(getHttpClient()).build();
        return retrofit.create(serviceClass);
    }

    private <S> Callback<S> getCallBack(final ResponseSuccessful<S> responseSucc,
                                        @Nullable final ResponseFail responseFail){
        return new Callback<S>() {
            @Override
            public void onResponse(Call<S> call, retrofit2.Response<S> response) {
                if (response.code() == 200) {
                    responseSucc.response(response.body());
                } else {
                    if (response.code() == 401)
                        setAuthToken(null);
                    if (responseFail != null) {
                        responseFail.onFail("");
                    }
                }
            }

            @Override
            public void onFailure(Call<S> call, Throwable t) {
                if (responseFail != null) {
                    responseFail.onFail("");
                }
            }
        };
    }

    public void GetEpisodes(final long seriesId,
                            String page,
                          ResponseSuccessful<Episodes> responseSucc,
                          @Nullable ResponseFail responseFail){
        Call<Episodes> responseCall = tvDbAuthApi.episodes(seriesId, page);
        responseCall.enqueue(getCallBack(responseSucc, responseFail));
    }

    public void GetActors(final long seriesId,
                                ResponseSuccessful<Actors> responseSucc,
                              @Nullable ResponseFail responseFail){
        Call<Actors> responseCall = tvDbAuthApi.actors(seriesId);
        responseCall.enqueue(getCallBack(responseSucc, responseFail));
    }

    public void GetSeriesInfo(final long seriesId,
                              ResponseSuccessful<SeriesResponse> responseSucc,
                              @Nullable ResponseFail responseFail){
        Call<SeriesResponse> responseCall = tvDbAuthApi.getSeries(seriesId);
        responseCall.enqueue(getCallBack(responseSucc, responseFail));
    }

    public void GetSeriesRecent(final String fromtime, final String toTime,
                                ResponseSuccessful<SeriesRecentResponse>  responseSucc,
                                @Nullable ResponseFail responseFail)
    {
        Call<SeriesRecentResponse> responseCall = tvDbAuthApi.seriesRecent(fromtime, toTime);
        responseCall.enqueue(getCallBack(responseSucc, responseFail));
    }

    public void Authentication(final ResponseSuccessful<String>  responseSucc,
                               @Nullable final ResponseFail responseFail){
        Login loginObj = new Login(apiKey, login, pass);
        Call<Token> token = tvDbAuthApi.login(loginObj);
        token.enqueue(new Callback<Token>(){

            @Override
            public void onResponse(Call<Token> call, retrofit2.Response<Token> response) {
                if (response.code() == 200){
                    authToken = response.body().token;

                    responseSucc.response(authToken);
                }else if (response.code() == 401){
                    if (responseFail != null) {
                        responseFail.onFail("");
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if (responseFail != null) {
                    responseFail.onFail("");
                }
            }
        });

    }

    public boolean validAuthToken() {
        return authToken != null;
    }
}