package mobi.wrapper.listings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.network.response.JSONGetCallback;
import mobi.wrapper.listings.controller.network.response.ResponseError;
import mobi.wrapper.listings.model.ShowContent.ShowContent;
import mobi.wrapper.listings.model.peopleCasting.PersonCasting;
import mobi.wrapper.listings.model.tvShows.AiringTodayTVShows;
import mobi.wrapper.listings.model.tvShows.PopularTVShows;
import mobi.wrapper.listings.model.tvShows.TVShows;
import mobi.wrapper.listings.model.tvShows.TopRatedTVShows;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 3/29/2016.
 */
public class ShowDetailsService extends TVListingBaseService {
    static final int POPULAR = 1;
    static final int TOP_RATED = 2;
    static final int AIRING_TODAY = 3;
    public void getShowDetail(int mId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.SHOW_URL, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<ShowContent>() {
                }.getType();
                ShowContent showData = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(showData);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
    public void personShowList(int mId, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.PEOPLE_CASTING, mId);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<PersonCasting>() {
                }.getType();
                PersonCasting personCasting = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(personCasting);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
    public void showsList(int mId, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.SIMILAR_SHOWS, mId);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<TVShows>() {
                }.getType();
                TVShows tvShows = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(tvShows);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
    public void showsListABC(final int type, final ServiceCallbacks callbacks) {

        String url = null;
        if(type == POPULAR) {
            url = UrlConstants.POPULAR_SHOWS;
        } else if(type == TOP_RATED) {
            url = UrlConstants.TOP_RATED_SHOWS;
        }else if (type == AIRING_TODAY) {
            url = UrlConstants.AIRING_TODAY_SHOWS;
        }
        if(url != null) {

            TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
                @Override
                public void onSuccess(JSONObject responseObject) {
                    String reader = responseObject.toString();
                    Type listType = new TypeToken<TVShows>() {
                    }.getType();
                    TVShows tvShows = new GsonBuilder().create().fromJson(reader, listType);
                    if(type == POPULAR) {
                        PopularTVShows popularTVShows = new PopularTVShows();
                        popularTVShows.tvShows = tvShows;
                        callbacks.onSuccess(popularTVShows);
                    } else if(type == TOP_RATED) {
                        TopRatedTVShows topRatedTVShows = new TopRatedTVShows();
                        topRatedTVShows.tvShows = tvShows;
                        callbacks.onSuccess(topRatedTVShows);
                    }else if (type == AIRING_TODAY) {
                        AiringTodayTVShows airingTodayTVShows = new AiringTodayTVShows();
                        airingTodayTVShows.tvShows = tvShows;
                        callbacks.onSuccess(airingTodayTVShows);
                    }
                }

                @Override
                public void onError(ResponseError error) {

                }
            });
        }
    }
}