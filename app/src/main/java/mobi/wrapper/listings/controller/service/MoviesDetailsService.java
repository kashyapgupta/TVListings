package mobi.wrapper.listings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.network.response.JSONGetCallback;
import mobi.wrapper.listings.controller.network.response.ResponseError;
import mobi.wrapper.listings.model.DiscoveredData;
import mobi.wrapper.listings.model.movieContents.MovieContent;
import mobi.wrapper.listings.model.movieContents.MoviesCollectionContent;
import mobi.wrapper.listings.model.moviesList.MoviesList;
import mobi.wrapper.listings.model.moviesList.NowPlayingMoviesList;
import mobi.wrapper.listings.model.moviesList.PopularMoviesList;
import mobi.wrapper.listings.model.moviesList.TopRatedMoviesList;
import mobi.wrapper.listings.model.moviesList.UpcomingMoviesList;

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
