package com.tvlistings.controller.network.response;

import org.json.JSONObject;

/**
 * Created by rohitgarg on 3/9/16.
 */
public abstract class JSONGetCallback extends GetCallback{

    public abstract void onSuccess(JSONObject responseObject);
}
