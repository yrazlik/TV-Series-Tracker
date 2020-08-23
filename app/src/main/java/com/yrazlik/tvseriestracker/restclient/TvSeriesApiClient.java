package com.yrazlik.tvseriestracker.restclient;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import com.yrazlik.tvseriestracker.restclient.TvSeriesApiInterceptor;
import com.yrazlik.tvseriestracker.restclient.TvSeriesApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yrazlik on 09/10/2017.
 */

public class TvSeriesApiClient {

    private static String apiBaseUrl = "https://api.tvmaze.com/";
    private static Retrofit retrofit = null;
    private static TvSeriesApiInterface mApiInterface = null;
    private static OkHttpClient.Builder builder;
    private static OkHttpClient httpClient;
    private static TvSeriesApiInterceptor apiInterceptor;

    public static Retrofit getClient() {
        if (retrofit == null) {
            apiInterceptor = new TvSeriesApiInterceptor();
            builder = new OkHttpClient.Builder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.addInterceptor(apiInterceptor);
            httpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    public static TvSeriesApiInterface getApiInterface() {
        if (mApiInterface == null) {
            mApiInterface = getClient().create(TvSeriesApiInterface.class);
        }
        return mApiInterface;
    }

    public static void cancelAllRequests() {
        if(mApiInterface == null) {
            mApiInterface = getClient().create(TvSeriesApiInterface.class);
        }
        httpClient.dispatcher().cancelAll();
    }


}
