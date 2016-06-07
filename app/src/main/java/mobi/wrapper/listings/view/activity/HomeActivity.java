package mobi.wrapper.listings.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.MoviesDetailsService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.controller.service.ShowDetailsService;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.ShowContent.ShowContent;
import mobi.wrapper.listings.model.movieContents.MovieContent;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.view.adapter.LikedMoviesRecyclerViewAdapter;
import mobi.wrapper.listings.view.adapter.LikedShowsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Random;

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

    @Bind(R.id.activity_home_main_relative_layout)
    RelativeLayout mMainRelativeLayout;

    @Bind(R.id.activity_home_background_network_image_view)
    NetworkImageView mBackgroundNetworkImageView;

    @Bind(R.id.activity_home_liked_show_recycler_view)
    RecyclerView mLikedShowsRecyclerView;

    @Bind(R.id.activity_home_liked_movies_recycler_view)
    RecyclerView mLikedMoviesRecyclerView;

    Context mContext;
    private LinearLayoutManager mLikedShowsLinearLayoutManager;
    private LinearLayoutManager mLikedMoviesLinearLayoutManager;
    Random mRandom;
    int mOldHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        if (!checkInternet(this)) {
            mMainRelativeLayout.removeAllViews();
            mMainRelativeLayout.setBackground(getResources().getDrawable(R.drawable.game_of_thrones));
            TextView textView = new TextView(this);
            textView.setTextSize(24);
            mMainRelativeLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            mMainRelativeLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            textView.setTextColor(getResources().getColor(R.color.textColor));
            textView.setText("Internet not connected.");
            mMainRelativeLayout.addView(textView);
        }else {
            mQueue = TVListingNetworkClient.getInstance().getRequestQueue();

            mLikedShowsRecyclerView.setHasFixedSize(true);
            mLikedShowsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mLikedShowsRecyclerView.setLayoutManager(mLikedShowsLinearLayoutManager);
            mLikedMoviesRecyclerView.setHasFixedSize(true);
            mLikedMoviesLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mLikedMoviesRecyclerView.setLayoutManager(mLikedMoviesLinearLayoutManager);
            mSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.design.widget.CoordinatorLayout.LayoutParams params1 = (CoordinatorLayout.LayoutParams) mMyAppBarLayout.getLayoutParams();
                    mOldHeight = params1.height;
                    android.support.design.widget.CoordinatorLayout.LayoutParams params = new android.support.design.widget.CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mMyAppBarLayout.setLayoutParams(params);
                }
            });
        }
    }

    public boolean checkInternet (Context context) {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnected() || mobile.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkInternet(mContext)) {
            mShowSharedPreferences = getSharedPreferences("showPreferences", MODE_PRIVATE);
            String shows = mShowSharedPreferences.getString("likedShows", "");
            String[] showsIds = shows.split(",");

            likedShows.clear();
            mLikedShowsData.clear();
            for (int i = 0; i < showsIds.length; i++) {
                if (showsIds[i] != "") {
                    likedShows.add(Integer.valueOf(showsIds[i]));
                }
            }
            if (likedShows.size() > 0) {
                TextView liked = (TextView) findViewById(R.id.activity_home_liked_show_text_view);
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_home_liked_show_frame_layout);
                liked.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                TextView liked = (TextView) findViewById(R.id.activity_home_liked_show_text_view);
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_home_liked_show_frame_layout);
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
            for (int i = 0; i < moviesId.length; i++) {
                if (moviesId[i] != "") {
                    likedMovies.add(Integer.valueOf(moviesId[i]));
                }
            }
            if (likedMovies.size() > 0) {
                TextView liked = (TextView) findViewById(R.id.activity_home_liked_movies_text_view);
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_home_liked_movies_frame_layout);
                liked.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                TextView liked = (TextView) findViewById(R.id.activity_home_liked_movies_text_view);
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_home_liked_movies_frame_layout);
                liked.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }

            for (int i = 0; i < likedMovies.size(); i++) {
                ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).getMovieDetail(likedMovies.get(i), HomeActivity.this);
            }

            if (!(likedMovies.size() > 0) && !(likedShows.size() > 0)) {
                TextView textView = (TextView) findViewById(R.id.activity_home_no_liked_text_view);
                textView.setVisibility(View.VISIBLE);
            } else {
                TextView textView = (TextView) findViewById(R.id.activity_home_no_liked_text_view);
                textView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    public void onSuccess(BaseResponse response) {
        mRandom = new Random();
        int type = mRandom.nextInt(2);
        if (response instanceof ShowContent) {
            mLikedShowsData.add((ShowContent) response);
            ProgressBar likedShowsProgressBar = (ProgressBar)findViewById(R.id.activity_home_liked_show_loading_progressBar);
            likedShowsProgressBar.setVisibility(View.GONE);
            if (mLikedShowsData.size() == likedShows.size()) {
                if (type == 0) {
                    int index = mRandom.nextInt(mLikedShowsData.size());
                    if (!TextUtils.isEmpty(mLikedShowsData.get(index).getPoster_path()) && !"null".equalsIgnoreCase(mLikedShowsData.get(index).getPoster_path())) {
                        mBackgroundNetworkImageView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mLikedShowsData.get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                    }
                }
                mLikedShowsAdapter = new LikedShowsRecyclerViewAdapter(mLikedShowsData, mQueue, mContext);
                mLikedShowsRecyclerView.setAdapter(mLikedShowsAdapter);
            }
        }else if (response instanceof MovieContent) {
            mLikedMoviesData.add((MovieContent) response);
            ProgressBar likedMoviesProgressBar = (ProgressBar)findViewById(R.id.activity_home_liked_movies_loading_progressBar);
            likedMoviesProgressBar.setVisibility(View.GONE);
            if (mLikedMoviesData.size() == likedMovies.size()) {
                if (type == 1) {
                    int index = mRandom.nextInt(mLikedMoviesData.size());
                    if (!TextUtils.isEmpty(mLikedMoviesData.get(index).getPoster_path()) && !"null".equalsIgnoreCase(mLikedMoviesData.get(index).getPoster_path())) {
                        mBackgroundNetworkImageView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mLikedMoviesData.get(index).getPoster_path()), TVListingNetworkClient.getInstance().getImageLoader());
                    }
                }
                mLikedMoviesAdapter = new LikedMoviesRecyclerViewAdapter(mLikedMoviesData, mQueue, mContext);
                mLikedMoviesRecyclerView.setAdapter(mLikedMoviesAdapter);
            }
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }

    @Override
    public void onBackPressed() {
        android.support.design.widget.CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mMyAppBarLayout.getLayoutParams();
        if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mCollapsingToolbarLayout.setTitle("TVListings");
            params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mOldHeight);
            mMyAppBarLayout.setLayoutParams(params);
            mMyAppBarLayout.setExpanded(false);
        }else {
            super.onBackPressed();
        }
    }
}