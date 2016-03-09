package com.tvlistings.controller.util.network;

import com.tvlistings.controller.network.response.ResponseError;

/**
 * Created by rohitgarg on 3/9/16.
 */
public class BaseResponse {

    private String statusCode;
    private String version;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private ResponseError error;


    public ResponseError getError() {
        return error;
    }

    public void setError(ResponseError error) {
        this.error = error;
    }


}