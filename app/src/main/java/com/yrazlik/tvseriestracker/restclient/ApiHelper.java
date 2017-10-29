package com.yrazlik.tvseriestracker.restclient;

import android.content.Context;
import android.util.Log;

import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.response.SearchResultResponse;
import com.yrazlik.tvseriestracker.util.Utils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class ApiHelper {

    private Context mContext;

    private static ApiHelper mInstance;

    public static ApiHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiHelper();
        }
        mInstance.setContext(context);
        return mInstance;
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private ApiHelper() {

    }

    private void onSuccessResponse(IApiResponseHandler responseHandler, Response response) {
        if(responseHandler != null) {
            try {
                responseHandler.onResponse(response.code(), response.errorBody().string(), response.body());
            } catch (IOException e) {
                responseHandler.onResponse(response.code(), null, response.body());
            } catch (Exception e) {
                responseHandler.onResponse(response.code(), null, response.body());
            }
        }
    }

    private void onFailResponse(final IApiResponseHandler responseHandler, Throwable t) {
        if(responseHandler != null) {
            responseHandler.onFail(t);
        }
    }

    public void searchShow(final String showName, final ApiResponseListener responseListener) {

        if(!Utils.isNullOrEmpty(showName)) {
            Call<List<SearchResultDto>> call = TvSeriesApiClient.getApiInterface().getSearchResult(showName);
            call.enqueue(new Callback<List<SearchResultDto>>() {
                @Override
                public void onResponse(Call<List<SearchResultDto>> call, Response<List<SearchResultDto>> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<List<SearchResultDto>> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }
}
