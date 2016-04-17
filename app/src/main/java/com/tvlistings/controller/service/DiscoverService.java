package com.tvlistings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.model.DiscoveredData;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 4/16/2016.
 */
public class DiscoverService extends TVListingBaseService {

    public void getDiscoveredMovies (String sortBy, int year, int pageNo, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.DISCOVER_MOVIES, sortBy, year, pageNo);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<DiscoveredData>() {
                }.getType();
                DiscoveredData discoveredData = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(discoveredData);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getNetworkShows (int id, int page, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.NETWORK_SHOWS, id, page);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<DiscoveredData>() {
                }.getType();
                DiscoveredData discoveredData = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(discoveredData);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
}
