package com.yrazlik.tvseriestracker.restclient;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yrazlik on 29/10/17.
 */

public class TvSeriesApiInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
               // .addHeader("X-Riot-Token", AppConstants.API_KEY_PROD)
                .build();
        return chain.proceed(newRequest);
    }
}
