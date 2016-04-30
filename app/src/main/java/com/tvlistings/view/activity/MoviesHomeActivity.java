package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.tvlistings.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
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
    public void onSuccess(BaseResponse response) {
        if (response instanceof PopularMoviesList) {
            mPopularMovies = ((PopularMoviesList) response).moviesList;
            ProgressBar popularProgressBar = (ProgressBar)findViewById(R.id.activity_movies_home_popular_loading_progressBar);
            popularProgressBar.setVisibility(View.GONE);
            TextView textView = (TextView)findViewById(R.id.activity_movies_home_no_popular_text_view);
            if (mPopularMovies.getResults().size() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
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
        Intent intent = new Intent(this, SelectedMovieActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
