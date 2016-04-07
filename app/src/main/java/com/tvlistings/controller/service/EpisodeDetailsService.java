package com.tvlistings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.model.episodes.Episodes;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 3/31/2016.
 */
public class EpisodeDetailsService extends TVListingBaseService {
    public void episodeDetails(int id, int seasonNo, int episodeNo, final ServiceCallbacks callbacks) {
        String urlEpisode = String.format(UrlConstants.EPISODE_DETAILS, id, seasonNo, episodeNo);
        TVListingNetworkClient.getInstance().get(urlEpisode, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Episodes>() {
                }.getType();
                Episodes episodes = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(episodes);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
}