package com.tvlistings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.model.genresList.MovieGenresList;
import com.tvlistings.model.genresList.ShowGenresList;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 4/18/2016.
 */
public class GenresService extends TVListingBaseService {

    public void getMovieGenres (final ServiceCallbacks callbacks) {
        String url = UrlConstants.MOVIE_GENRES;
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<MovieGenresList>() {
                }.getType();
                MovieGenresList movieGenresList = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(movieGenresList);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getShowGenres (final ServiceCallbacks callbacks) {
        String url = UrlConstants.TV_GENRES;
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<ShowGenresList>() {
                }.getType();
                ShowGenresList showGenresList = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(showGenresList);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
}
