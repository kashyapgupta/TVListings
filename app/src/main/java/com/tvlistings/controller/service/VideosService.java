package com.tvlistings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.model.images.Images;
import com.tvlistings.model.videos.Videos;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 4/30/2016.
 */
public class VideosService extends TVListingBaseService {

    public void getShowVideos (int mId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.SHOW_VIDEOS, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Videos>() {
                }.getType();
                Videos videos = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(videos);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getSeasonVideos (int mId, int mSeasonId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.SEASON_VIDEOS, mId, mSeasonId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Videos>() {
                }.getType();
                Videos videos = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(videos);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getMovieVideos (int mId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.MOVIE_VIDEOS, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Videos>() {
                }.getType();
                Videos videos = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(videos);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getEpisodeVideos (int mId, int mSeasonNo, int mEpisodeNo, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.EPISODE_VIDEOS, mId, mSeasonNo, mEpisodeNo);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Videos>() {
                }.getType();
                Videos videos = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(videos);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
}
