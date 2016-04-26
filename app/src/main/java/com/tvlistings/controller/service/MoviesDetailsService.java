package com.tvlistings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.model.DiscoveredData;
import com.tvlistings.model.ShowContent.ShowContent;
import com.tvlistings.model.movieContents.MovieContent;
import com.tvlistings.model.movieContents.MoviesCollectionContent;
import com.tvlistings.model.moviesList.MoviesList;
import com.tvlistings.model.moviesList.NowPlayingMoviesList;
import com.tvlistings.model.moviesList.PopularMoviesList;
import com.tvlistings.model.moviesList.TopRatedMoviesList;
import com.tvlistings.model.moviesList.UpcomingMoviesList;
import com.tvlistings.model.tvShows.TVShows;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 4/8/2016.
 */
public class MoviesDetailsService extends TVListingBaseService {
    static final int TOP_RATED = 1;
    static final int POPULAR = 2;
    static final int UPCOMING = 3;
    static final int NOW_PLAYING = 4;

    public void getMovieDetail (int mId, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.MOVIE_URL, mId);
        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<MovieContent>() {
                }.getType();
                MovieContent movieContent = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(movieContent);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void moviesList(int mId, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.SIMILAR_MOVIES, mId);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<MoviesList>() {
                }.getType();
                MoviesList moviesList = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(moviesList);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getCollectionDetail (int id, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.MOVIE_COLLECTION_URL, id);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<MoviesCollectionContent>(){}.getType();
                MoviesCollectionContent moviesCollectionContent = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(moviesCollectionContent);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void moviesListABC (final int type, final ServiceCallbacks callbacks) {

        String url = null;
        if(type == POPULAR) {
            url = UrlConstants.POPULAR_MOVIES;
        } else if(type == TOP_RATED) {
            url = UrlConstants.TOP_RATED_MOVIES;
        }else if (type == UPCOMING) {
            url = UrlConstants.UPCOMING_MOVIES;
        }else if (type == NOW_PLAYING) {
            url = UrlConstants.NOW_PLAYING_MOVIES;
        }
        if(url != null) {

            TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
                @Override
                public void onSuccess(JSONObject responseObject) {
                    String reader = responseObject.toString();
                    Type listType = new TypeToken<MoviesList>() {
                    }.getType();
                    MoviesList moviesList = new GsonBuilder().create().fromJson(reader, listType);
                    if(type == POPULAR) {
                        PopularMoviesList popularMoviesList = new PopularMoviesList();
                        popularMoviesList.moviesList = moviesList;
                        callbacks.onSuccess(popularMoviesList);
                    } else if(type == TOP_RATED) {
                        TopRatedMoviesList topRatedMoviesList = new TopRatedMoviesList();
                        topRatedMoviesList.moviesList = moviesList;
                        callbacks.onSuccess(topRatedMoviesList);
                    }else if (type == UPCOMING) {
                        UpcomingMoviesList upcomingMoviesList = new UpcomingMoviesList();
                        upcomingMoviesList.moviesList = moviesList;
                        callbacks.onSuccess(upcomingMoviesList);
                    }else if (type == NOW_PLAYING) {
                        NowPlayingMoviesList nowPlayingMoviesList = new NowPlayingMoviesList();
                        nowPlayingMoviesList.moviesList = moviesList;
                        callbacks.onSuccess(nowPlayingMoviesList);
                    }
                }

                @Override
                public void onError(ResponseError error) {

                }
            });
        }
    }

    public void getProductionCompanyMovies (int id, int page, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.PRODUCTION_COMPANY_MOVIES, id, page+1);

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
