package com.tvlistings.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.MoviesDetailsService;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.moviesList.MoviesList;
import com.tvlistings.model.moviesList.NowPlayingMoviesList;
import com.tvlistings.model.moviesList.PopularMoviesList;
import com.tvlistings.model.moviesList.TopRatedMoviesList;
import com.tvlistings.model.moviesList.UpcomingMoviesList;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.MoviesRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayMovie;

import java.util.Random;

import butterknife.Bind;

/**
 * Created by Rohit on 4/7/2016.
 */
public class MoviesHomeActivity extends BaseSearchActivity implements DisplayMovie {

    private MoviesList mPopularMovies;
    private MoviesList mTopRatedMovies;
    private MoviesList mNowPlayingMovies;
    private MoviesList mUpcomingMovies;

    @Bind(R.id.activity_movies_home_popular_recycler_view)
    RecyclerView mPopularMoviesRecyclerView;

    @Bind(R.id.activity_movies_home_trending_recycler_view)
    RecyclerView mTopRatedMoviesRecyclerView;

    @Bind(R.id.activity_movies_home_background_network_image_view)
    NetworkImageView mBackgroundView;

    @Bind(R.id.activity_movies_home_main_relative_layout)
    RelativeLayout mMainRelativeLayout;

    @Bind(R.id.activity_movies_home_now_playing_recycler_view)
    RecyclerView mNowPlayingMoviesRecyclerView;

    @Bind(R.id.activity_movies_home_upcoming_recycler_view)
    RecyclerView mUpcomingMoviesRecyclerView;

    LinearLayoutManager mPopularMoviesLinearLayoutManager;
    LinearLayoutManager mTopRatedMoviesLinearLayoutManager;
    LinearLayoutManager mNowPlayingMoviesLinearLayoutManager;
    LinearLayoutManager mUpcomingMoviesLinearLayoutManager;

    MoviesRecyclerViewAdapter mPopularMoviesAdapter;
    MoviesRecyclerViewAdapter mTopRatedMoviesAdapter;
    MoviesRecyclerViewAdapter mNowPlayingMoviesAdapter;
    MoviesRecyclerViewAdapter mUpcomingMoviesAdapter;

    Context mContext;
    RequestQueue mQueue;
    ImageLoader mImageLoader;
    Random mRandom;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        mMainRelativeLayout.setNestedScrollingEnabled(true);

        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();

        mPopularMoviesRecyclerView.setHasFixedSize(true);
        mTopRatedMoviesRecyclerView.setHasFixedSize(true);
        mUpcomingMoviesRecyclerView.setHasFixedSize(true);
        mNowPlayingMoviesRecyclerView.setHasFixedSize(true);

        mPopularMoviesLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mNowPlayingMoviesLinearLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mUpcomingMoviesLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTopRatedMoviesLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mPopularMoviesRecyclerView.setLayoutManager(mPopularMoviesLinearLayoutManager);
        mTopRatedMoviesRecyclerView.setLayoutManager(mTopRatedMoviesLinearLayoutManager);
        mUpcomingMoviesRecyclerView.setLayoutManager(mUpcomingMoviesLinearLayoutManager);
        mNowPlayingMoviesRecyclerView.setLayoutManager(mNowPlayingMoviesLinearLayoutManager);

        //TOP_RATED
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).moviesListABC(1, MoviesHomeActivity.this);

        //POPULAR
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).moviesListABC(2, MoviesHomeActivity.this);

        //UPCOMING
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).moviesListABC(3, MoviesHomeActivity.this);

        //NOW_PLAYING
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).moviesListABC(4, MoviesHomeActivity.this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_movies_home;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRandom = new Random();
        int type = mRandom.nextInt(4);
        if (type == 0) {
            if (mPopularMovies != null) {
                int index = mRandom.nextInt(mPopularMovies.getResults().size());
                if (!TextUtils.isEmpty(mPopularMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mPopularMovies.getResults().get(index).getPoster_path())) {
                    mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mPopularMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                }
            }
        }else if (type == 1) {
            if (mTopRatedMovies != null) {
                int index = mRandom.nextInt(mTopRatedMovies.getResults().size());
                if (!TextUtils.isEmpty(mTopRatedMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mTopRatedMovies.getResults().get(index).getPoster_path())) {
                    mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mTopRatedMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                }
            }
        }else if (type == 2) {
            if (mNowPlayingMovies != null) {
                int index = mRandom.nextInt(mNowPlayingMovies.getResults().size());
                if (!TextUtils.isEmpty(mNowPlayingMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mNowPlayingMovies.getResults().get(index).getPoster_path())) {
                    mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mNowPlayingMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                }
            }
        }else if (type == 3) {
            if (mUpcomingMovies != null) {
                int index = mRandom.nextInt(mUpcomingMovies.getResults().size());
                if (!TextUtils.isEmpty(mUpcomingMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mUpcomingMovies.getResults().get(index).getPoster_path())) {
                    mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mUpcomingMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                }
            }
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        mRandom = new Random();
        int type = mRandom.nextInt(4);
        if (response instanceof PopularMoviesList) {
            mPopularMovies = ((PopularMoviesList) response).moviesList;
            ProgressBar popularProgressBar = (ProgressBar)findViewById(R.id.activity_movies_home_popular_loading_progressBar);
            popularProgressBar.setVisibility(View.GONE);
            TextView textView = (TextView)findViewById(R.id.activity_movies_home_no_popular_text_view);
            if (mPopularMovies.getResults().size() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
                if (type == 0) {
                    int index = mRandom.nextInt(mPopularMovies.getResults().size());
                    if (!TextUtils.isEmpty(mPopularMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mPopularMovies.getResults().get(index).getPoster_path())) {
                        mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mPopularMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                    }
                }
                textView.setVisibility(View.GONE);
            }
            mPopularMoviesAdapter = new MoviesRecyclerViewAdapter(mPopularMovies, mQueue, mContext);
            mPopularMoviesRecyclerView.setAdapter(mPopularMoviesAdapter);
        }else if (response instanceof TopRatedMoviesList) {
            mTopRatedMovies = ((TopRatedMoviesList) response).moviesList;
            ProgressBar topRatedProgressBar = (ProgressBar)findViewById(R.id.activity_movies_home_top_rated_loading_progressBar);
            topRatedProgressBar.setVisibility(View.GONE);
            TextView textView = (TextView)findViewById(R.id.activity_movies_home_no_top_rated_text_view);
            if (mTopRatedMovies.getResults().size() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
                if (type == 1) {
                    int index = mRandom.nextInt(mTopRatedMovies.getResults().size());
                    if (!TextUtils.isEmpty(mTopRatedMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mTopRatedMovies.getResults().get(index).getPoster_path())) {
                        mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mTopRatedMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                    }
                }
                textView.setVisibility(View.GONE);
            }
            mTopRatedMoviesAdapter = new MoviesRecyclerViewAdapter(mTopRatedMovies, mQueue, mContext);
            mTopRatedMoviesRecyclerView.setAdapter(mTopRatedMoviesAdapter);
        }else if (response instanceof NowPlayingMoviesList) {
            mNowPlayingMovies = ((NowPlayingMoviesList) response).moviesList;
            ProgressBar nowPlayingProgressBar = (ProgressBar)findViewById(R.id.activity_movies_home_now_playing_loading_progressBar);
            nowPlayingProgressBar.setVisibility(View.GONE);
            TextView textView = (TextView)findViewById(R.id.activity_movies_home_no_now_playing_text_view);
            if (mNowPlayingMovies.getResults().size() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
                if (type == 2) {
                    int index = mRandom.nextInt(mNowPlayingMovies.getResults().size());
                    if (!TextUtils.isEmpty(mNowPlayingMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mNowPlayingMovies.getResults().get(index).getPoster_path())) {
                        mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mNowPlayingMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                    }
                }
                textView.setVisibility(View.GONE);
            }
            mNowPlayingMoviesAdapter = new MoviesRecyclerViewAdapter(mNowPlayingMovies, mQueue, mContext);
            mNowPlayingMoviesRecyclerView.setAdapter(mNowPlayingMoviesAdapter);
        }else if (response instanceof UpcomingMoviesList) {
            mUpcomingMovies = ((UpcomingMoviesList) response).moviesList;
            ProgressBar upComingProgressBar = (ProgressBar)findViewById(R.id.activity_movies_home_upcoming_loading_progressBar);
            upComingProgressBar.setVisibility(View.GONE);
            TextView textView = (TextView)findViewById(R.id.activity_movies_home_no_upcoming_text_view);
            if (mUpcomingMovies.getResults().size() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
                if (type == 3) {
                    int index = mRandom.nextInt(mUpcomingMovies.getResults().size());
                    if (!TextUtils.isEmpty(mUpcomingMovies.getResults().get(index).getPoster_path()) && !"null".equalsIgnoreCase(mUpcomingMovies.getResults().get(index).getPoster_path())) {
                        mBackgroundView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mUpcomingMovies.getResults().get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                    }
                }
                textView.setVisibility(View.GONE);
            }
            mUpcomingMoviesAdapter = new MoviesRecyclerViewAdapter(mUpcomingMovies, mQueue, mContext);
            mUpcomingMoviesRecyclerView.setAdapter(mUpcomingMoviesAdapter);
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }

    @Override
    public void displayMovie(int id) {
        Log.i("movie ID", String.valueOf(id));
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
