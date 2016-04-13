package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.MoviesDetailsService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.controller.service.ShowDetailsService;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.ShowContent.ShowContent;
import com.tvlistings.model.movieContents.MovieContent;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.LikedMoviesRecyclerViewAdapter;
import com.tvlistings.view.adapter.LikedShowsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Rohit on 4/9/2016.
 */
public class HomeActivity extends  BaseSearchActivity implements ServiceCallbacks {

    ArrayList<ShowContent> mLikedShowsData = new ArrayList<>();
    ArrayList<Integer> likedShows = new ArrayList<>();
    SharedPreferences mShowSharedPreferences;

    ArrayList<MovieContent> mLikedMoviesData = new ArrayList<>();
    ArrayList<Integer> likedMovies = new ArrayList<>();
    SharedPreferences mMoviesSharedPreferences;

    RequestQueue mQueue;

    private LikedShowsRecyclerViewAdapter mLikedShowsAdapter;
    private LikedMoviesRecyclerViewAdapter mLikedMoviesAdapter;

    @Bind(R.id.activity_home_tv_show_image_view)
    ImageView mTvShow;

    @Bind(R.id.activity_home_movies_image_view)
    ImageView mMovie;

    @Bind(R.id.activity_home_people_image_view)
    ImageView mPeople;

    @Bind(R.id.activity_home_tv_show_text_view)
    TextView mTvShowsTextView;

    @Bind(R.id.activity_home_movies_text_view)
    TextView mMoviesTextView;

    @Bind(R.id.activity_home_people_text_view)
    TextView mPeopleTextView;

    @Bind(R.id.activity_home_liked_show_recycler_view)
    RecyclerView mLikedShowsRecyclerView;

    @Bind(R.id.activity_home_liked_movies_recycler_view)
    RecyclerView mLikedMoviesRecyclerView;

    Context mContext;
    private LinearLayoutManager mLikedShowsLinearLayoutManager;
    private LinearLayoutManager mLikedMoviesLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);

        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();

        mLikedShowsRecyclerView.setHasFixedSize(true);
        mLikedShowsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLikedShowsRecyclerView.setLayoutManager(mLikedShowsLinearLayoutManager);

        mLikedMoviesRecyclerView.setHasFixedSize(true);
        mLikedMoviesLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLikedMoviesRecyclerView.setLayoutManager(mLikedMoviesLinearLayoutManager);

        mTvShow.setImageResource(R.mipmap.ic_tv_white_48dp);
        mMovie.setImageResource(R.mipmap.ic_movie_white_48dp);
        mPeople.setImageResource(R.mipmap.ic_people_outline_white_48dp);

        mTvShowsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TVShowsHomeActivity.class);
                startActivity(intent);
            }
        });

        mMoviesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MoviesHomeActivity.class);
                startActivity(intent);
            }
        });

        mPeopleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mShowSharedPreferences = getSharedPreferences("showPreferences", MODE_PRIVATE);
        String shows = mShowSharedPreferences.getString("likedShows", "");
        String[] showsIds = shows.split(",");

        likedShows.clear();
        mLikedShowsData.clear();
        for (int i = 0; i <showsIds.length; i++) {
            if (showsIds[i] != "") {
                likedShows.add(Integer.valueOf(showsIds[i]));
            }
        }
        if (likedShows.size() > 0) {
            TextView liked = (TextView)findViewById(R.id.activity_home_liked_show_text_view);
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_home_liked_show_frame_layout);
            liked.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
        }else {
            TextView liked = (TextView)findViewById(R.id.activity_home_liked_show_text_view);
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_home_liked_show_frame_layout);
            liked.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < likedShows.size(); i++) {
            ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).getShowDetail(likedShows.get(i), HomeActivity.this);
        }

        mMoviesSharedPreferences = getSharedPreferences("moviesPreferences", MODE_PRIVATE);
        String movies = mMoviesSharedPreferences.getString("likedMovies", "");
        String[] moviesId = movies.split(",");

        likedMovies.clear();
        mLikedMoviesData.clear();
        for (int i = 0; i <moviesId.length; i++) {
            if (moviesId[i] != "") {
                likedMovies.add(Integer.valueOf(moviesId[i]));
            }
        }
        if (likedMovies.size() > 0) {
            TextView liked = (TextView)findViewById(R.id.activity_home_liked_movies_text_view);
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_home_liked_movies_frame_layout);
            liked.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
        }else {
            TextView liked = (TextView)findViewById(R.id.activity_home_liked_movies_text_view);
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_home_liked_movies_frame_layout);
            liked.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < likedMovies.size(); i++) {
            ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).getMovieDetail(likedMovies.get(i), HomeActivity.this);
        }

        if (!(likedMovies.size() > 0) && !(likedShows.size() > 0)) {
            TextView textView = (TextView)findViewById(R.id.activity_home_no_liked_text_view);
            textView.setVisibility(View.VISIBLE);
        }else {
            TextView textView = (TextView)findViewById(R.id.activity_home_no_liked_text_view);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof ShowContent) {
            mLikedShowsData.add((ShowContent) response);
            ProgressBar likedShowsProgressBar = (ProgressBar)findViewById(R.id.activity_home_liked_show_loading_progressBar);
            likedShowsProgressBar.setVisibility(View.GONE);
            if (mLikedShowsData.size() == likedShows.size()) {
                mLikedShowsAdapter = new LikedShowsRecyclerViewAdapter(mLikedShowsData, mQueue, mContext);
                mLikedShowsRecyclerView.setAdapter(mLikedShowsAdapter);
            }
        }else if (response instanceof MovieContent) {
            mLikedMoviesData.add((MovieContent) response);
            ProgressBar likedMoviesProgressBar = (ProgressBar)findViewById(R.id.activity_home_liked_movies_loading_progressBar);
            likedMoviesProgressBar.setVisibility(View.GONE);
            if (mLikedMoviesData.size() == likedMovies.size()) {
                mLikedMoviesAdapter = new LikedMoviesRecyclerViewAdapter(mLikedMoviesData, mQueue, mContext);
                mLikedMoviesRecyclerView.setAdapter(mLikedMoviesAdapter);
            }
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }
}