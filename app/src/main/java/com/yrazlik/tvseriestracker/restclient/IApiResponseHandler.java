package com.yrazlik.tvseriestracker.restclient;

/**
 * Created by yrazlik on 29.10.2017.
 */

public interface IApiResponseHandler {

    void onResponse(int responseCode, String errorMessage, Object response);
    void onFail(Throwable t);
}
