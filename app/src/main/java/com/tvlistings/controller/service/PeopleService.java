package com.tvlistings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.model.PersonDetails;
import com.tvlistings.model.PopularPeople;
import com.tvlistings.model.peopleCasting.PersonCasting;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 3/30/2016.
 */
public class PeopleService extends TVListingBaseService {
    public void getShowCast(int mId, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.SHOW_PEOPLE_URL, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<PersonCasting>() {
                }.getType();
                PersonCasting peopleCastingShow = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(peopleCastingShow);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getMovieCast(int mId, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.MOVIE_PEOPLE_URL, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<PersonCasting>() {
                }.getType();
                PersonCasting peopleCastingShow = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(peopleCastingShow);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getPersonDetails(int mId, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.PEOPLE_DETAILS, mId);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<PersonDetails>() {
                }.getType();
                PersonDetails personDetails = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(personDetails);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getPopularPeople(int mCurrentPage, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.POPULAR_PEOPLE, mCurrentPage);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<PopularPeople>() {
                }.getType();
                PopularPeople popularPersons = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(popularPersons);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

}
