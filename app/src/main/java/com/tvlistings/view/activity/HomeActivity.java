package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.controller.SendNotification;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.controller.service.ShowDetailsService;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.ShowContent.ShowContent;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.model.tvShows.AiringTodayTVShows;
import com.tvlistings.model.tvShows.PopularTVShows;
import com.tvlistings.model.tvShows.TVShows;
import com.tvlistings.model.tvShows.TopRatedTVShows;
import com.tvlistings.view.adapter.LikedShowsRecyclerViewAdapter;
import com.tvlistings.view.adapter.TVShowsRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayShow;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Rohit on 3/14/2016.
 */

public class HomeActivity extends BaseSearchActivity implements DisplayShow, ServiceCallbacks{

    private RequestQueue mQueue2;
    private TVShows mTopRatedShows;
    private TVShows mPopularShows;
    private TVShows mAiringTodayShows;
    private TVShowsRecyclerViewAdapter mTopRatedAdapter;
    private TVShowsRecyclerViewAdapter mPopularAdapter;
    private TVShowsRecyclerViewAdapter mAiringTodayAdapter;
    private LikedShowsRecyclerViewAdapter mLikedShowsAdapter;
    @Bind(R.id.activity_home_trending_recycler_view)
    RecyclerView mTrendingRecyclerView;
    @Bind(R.id.activity_home_popular_recycler_view)
    RecyclerView mPopularRecyclerView;
    @Bind(R.id.activity_home_airing_today_recycler_view)
    RecyclerView mAiringTodayRecyclerView;
    @Bind(R.id.activity_home_liked_show_recycler_view)
    RecyclerView mLikedShowsRecyclerView;
    Context mContext;
    ArrayList<ShowContent> mLikedShowsData = new ArrayList<>();
    ArrayList<Integer> likedShows = new ArrayList<>();
    SharedPreferences mSharedPreferences;
    private LinearLayoutManager mTopRatedLinearLayoutManager;
    private LinearLayoutManager mPopularLinearLayoutManager;
    private LinearLayoutManager mAiringTodayLinearLayoutManager;
    private LinearLayoutManager mLikedShowsLinearLayoutManager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        mQueue2 = TVListingNetworkClient.getInstance().getRequestQueue();
        mTrendingRecyclerView.setHasFixedSize(true);
        mPopularRecyclerView.setHasFixedSize(true);
        mAiringTodayRecyclerView.setHasFixedSize(true);
        mLikedShowsRecyclerView.setHasFixedSize(true);

        mPopularLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTopRatedLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAiringTodayLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLikedShowsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPopularRecyclerView.setLayoutManager(mPopularLinearLayoutManager);
        mTrendingRecyclerView.setLayoutManager(mTopRatedLinearLayoutManager);
        mAiringTodayRecyclerView.setLayoutManager(mAiringTodayLinearLayoutManager);
        mLikedShowsRecyclerView.setLayoutManager(mLikedShowsLinearLayoutManager);

        //popularShows
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).showsListABC(1, HomeActivity.this);
        //TopRated
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).showsListABC(2, HomeActivity.this);
        //AiringToday
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).showsListABC(3, HomeActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSharedPreferences = getSharedPreferences("showPreferences", MODE_PRIVATE);
        String shows = mSharedPreferences.getString("likedShows", "");
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
    }

    @Override
    public void displayShow(int id, double rating) {
        Intent intent = new Intent(this, SelectedShowActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("rating", rating);
        startActivity(intent);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof PopularTVShows) {
            mPopularShows = ((PopularTVShows) response).tvShows;
            ProgressBar popularProgressBar = (ProgressBar) findViewById(R.id.Activity_home_popular_loading_progressBar);
            popularProgressBar.setVisibility(View.GONE);
            mPopularAdapter = new TVShowsRecyclerViewAdapter(mPopularShows, mQueue2, mContext);
            mPopularRecyclerView.setAdapter(mPopularAdapter);
        }else if (response instanceof TopRatedTVShows) {
            mTopRatedShows = ((TopRatedTVShows) response).tvShows;
            ProgressBar topRatedProgressBar = (ProgressBar) findViewById(R.id.Activity_home_top_rated_loading_progressBar);
            topRatedProgressBar.setVisibility(View.GONE);
            mTopRatedAdapter = new TVShowsRecyclerViewAdapter(mTopRatedShows, mQueue2, mContext);
            mTrendingRecyclerView.setAdapter(mTopRatedAdapter);
        }else if (response instanceof AiringTodayTVShows) {
            mAiringTodayShows = ((AiringTodayTVShows) response).tvShows;
            ProgressBar airingTodayProgressBar = (ProgressBar) findViewById(R.id.Activity_home_airing_today_loading_progressBar);
            for (int i = 0; i < likedShows.size(); i++) {
                for (int j = 0; j < mAiringTodayShows.getResults().size(); j++) {
                    if (likedShows.get(i) == mAiringTodayShows.getResults().get(j).getId()) {
                        SendNotification notification = new SendNotification();
                        notification.setNotificationData(this, mAiringTodayShows.getResults().get(j).getName()+" airs today.");
                    }
                }
            }
            airingTodayProgressBar.setVisibility(View.GONE);
            mAiringTodayAdapter = new TVShowsRecyclerViewAdapter(mAiringTodayShows, mQueue2, mContext);
            mAiringTodayRecyclerView.setAdapter(mAiringTodayAdapter);
        }else if (response instanceof ShowContent) {
            mLikedShowsData.add((ShowContent) response);
            ProgressBar likedShowsProgressBar = (ProgressBar)findViewById(R.id.activity_home_liked_show_loading_progressBar);
            likedShowsProgressBar.setVisibility(View.GONE);
            if (mLikedShowsData.size() == likedShows.size()) {
                mLikedShowsAdapter = new LikedShowsRecyclerViewAdapter(mLikedShowsData, mQueue2, mContext);
                mLikedShowsRecyclerView.setAdapter(mLikedShowsAdapter);
            }
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }
}
