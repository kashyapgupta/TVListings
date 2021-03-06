package mobi.wrapper.listings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.network.response.JSONGetCallback;
import mobi.wrapper.listings.controller.network.response.ResponseError;
import mobi.wrapper.listings.model.videos.Videos;

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
