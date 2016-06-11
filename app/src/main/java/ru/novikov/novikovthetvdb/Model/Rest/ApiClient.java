package ru.novikov.novikovthetvdb.Model.Rest;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesRecentResponse;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.TvDbRestApi;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Requests.Login;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Token;


public class ApiClient {

    private static final String TAG = "ApiClient";
    private static final String TVDB_BASE_URL = "https://api.thetvdb.com";
    private static final String TVDB_VERSION = "2";
    private static final String TVDB_APIKEY = "3349F1D32F314DE9";
    public static final Object TAG_CALL = new Object();

    private final String pass;
    private final String login;

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private Retrofit.Builder builder;
    private NetworkFail networkFail;

    private String authToken = null;

    public ApiClient(final String login, final String pass) {
        builder = new Retrofit.Builder()
                        .baseUrl(TVDB_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        this.login = login;
        this.pass = pass;
    }


    public <S> S createService(Class<S> serviceClass) {
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        //.header("Content-Type", "application/json")
                        .header("Accept", "application/json") //vnd.thetvdb.v" + TVDB_VERSION
                        .method(original.method(), original.body())
                        .tag(TAG_CALL);

                if (authToken != null){
                    requestBuilder.header("Authorization", "Bearer " + authToken);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public boolean CheckAuth(final AfterLogin afterLogin){
        if (authToken != null){
            afterLogin.loginSucses();
            return true;
        }else{
            Authentication(login, pass, new ResponseSuccessful<String>() {
                @Override
                public void response(String body) {
                    afterLogin.loginSucses();
                }
            });
            return false;
        }
    }

    public void GetHeadSeries(final long seriesId, final ResponseSuccessful<SeriesResponse> responseSucc){
        TvDbRestApi tvDbRestApi = createService(TvDbRestApi.class);
        Call<SeriesResponse> responseCall = tvDbRestApi.getSeries(seriesId);
        responseCall.enqueue(new Callback<SeriesResponse>() {
            @Override
            public void onResponse(Call<SeriesResponse> call, retrofit2.Response<SeriesResponse> response) {
                if (response.code() == 200){
                    responseSucc.response(response.body());
                }
            }

            @Override
            public void onFailure(Call<SeriesResponse> call, Throwable t) {
                networkFail.onFail("");
            }
        });
    }

    public void GetSeriesRecent(final String fromtime, final String toTime, final ResponseSuccessful<SeriesRecentResponse>  responseSucc)
    {
        TvDbRestApi tvDbRestApi = createService(TvDbRestApi.class);
        Call<SeriesRecentResponse> responseCall = tvDbRestApi.seriesRecent(fromtime, toTime);
        responseCall.enqueue(new Callback<SeriesRecentResponse>() {
            @Override
            public void onResponse(Call<SeriesRecentResponse> call, retrofit2.Response<SeriesRecentResponse> response) {
                if (response.code() == 200){
                    responseSucc.response(response.body());
                }

            }

            @Override
            public void onFailure(Call<SeriesRecentResponse> call, Throwable t) {
                networkFail.onFail("");
            }
        });
    }

    public void Authentication(final String username, final String userkey, final ResponseSuccessful<String>  responseSucc){
        Login login = new Login(TVDB_APIKEY, username, userkey);
        TvDbRestApi tvDbRestApi = createService(TvDbRestApi.class);
        Call<Token> token = tvDbRestApi.login(login);
        token.enqueue(new Callback<Token>(){

            @Override
            public void onResponse(Call<Token> call, retrofit2.Response<Token> response) {
                if (response.code() == 200){
                    authToken = response.body().token;
                    responseSucc.response(authToken);
                }else if (response.code() == 401){
                    Log.e(TAG, "Error auth");
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                networkFail.onFail("");
            }
        });

    }
}