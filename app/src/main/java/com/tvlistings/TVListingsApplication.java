package com.tvlistings;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.util.RandomString;

/**
 * Created by rohitgarg on 3/9/16.
 */
public class TVListingsApplication extends Application {
    public static final String TAG = TVListingsApplication.class.getSimpleName();
    public static RandomString randomString = new RandomString(6);
    public static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        TVListingNetworkClient.init(this);
        gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
    }
}
