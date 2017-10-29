package com.yrazlik.tvseriestracker.restclient.error;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class TVSeriesApiError {

    private long errorCode;
    private String errorMessage;

    public TVSeriesApiError(long errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
