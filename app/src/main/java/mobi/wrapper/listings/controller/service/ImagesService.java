package mobi.wrapper.listings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.network.response.JSONGetCallback;
import mobi.wrapper.listings.controller.network.response.ResponseError;
import mobi.wrapper.listings.model.images.Images;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 4/27/2016.
 */
public class ImagesService extends TVListingBaseService {

    public void getShowImages (int mId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.SHOW_IMAGES, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Images>() {
                }.getType();
                Images images = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(images);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getMovieImages (int mId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.MOVIE_IMAGES, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Images>() {
                }.getType();
                Images images = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(images);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getPersonImages (int mId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.PERSON_IMAGES, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<Images>() {
                }.getType();
                Images images = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(images);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
}
