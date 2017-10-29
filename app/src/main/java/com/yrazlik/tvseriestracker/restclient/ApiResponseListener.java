package com.yrazlik.tvseriestracker.restclient;

import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;

/**
 * Created by yrazlik on 29.10.2017.
 */

public interface ApiResponseListener {

    void onResponse(Object response);
    void onFail(TVSeriesApiError apiError);
}
