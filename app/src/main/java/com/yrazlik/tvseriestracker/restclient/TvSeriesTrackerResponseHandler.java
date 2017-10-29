package com.yrazlik.tvseriestracker.restclient;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.TvSeriesTrackerApp;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class TvSeriesTrackerResponseHandler implements IApiResponseHandler {

    private ApiResponseListener apiResponseListener;

    private TvSeriesTrackerResponseHandler() {
    }

    public TvSeriesTrackerResponseHandler(ApiResponseListener apiResponseListener) {
        this.apiResponseListener = apiResponseListener;
    }


    @Override
    public void onResponse(int responseCode, String errorMessage, Object response) {
        if (apiResponseListener != null) {
            if (response != null) {
                if (responseCode == 200) {
                    apiResponseListener.onResponse(response);
                } else {
                    TVSeriesApiError apiError = new TVSeriesApiError(0,
                            TvSeriesTrackerApp.getAppContext().getResources().getString(R.string.unknown_error));
                    apiResponseListener.onFail(apiError);
                }
            } else {
                TVSeriesApiError apiError = new TVSeriesApiError(0,
                        TvSeriesTrackerApp.getAppContext().getResources().getString(R.string.unknown_error));
                apiResponseListener.onFail(apiError);
            }
        }
    }

    @Override
    public void onFail(Throwable t) {
        if (t != null) {
            try {
                TVSeriesApiError apiError = new TVSeriesApiError(0,
                        t.getMessage());
                apiResponseListener.onFail(apiError);
            } catch (Exception e) {
                TVSeriesApiError apiError = new TVSeriesApiError(0,
                        TvSeriesTrackerApp.getAppContext().getResources().getString(R.string.unknown_error));
                apiResponseListener.onFail(apiError);
            }
        } else {
            TVSeriesApiError apiError = new TVSeriesApiError(0,
                    TvSeriesTrackerApp.getAppContext().getResources().getString(R.string.unknown_error));
            apiResponseListener.onFail(apiError);
        }
    }

}
