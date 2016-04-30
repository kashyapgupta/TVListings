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

public class TVShowsHomeActivity extends BaseSearchActivity implements DisplayShow, ServiceCallbacks{

    private RequestQueue mQueue2;
    private TVShows mTopRatedShows;
    private TVShows mPopularShows;
    private TVShows mAiringTodayShows;
    private TVShowsRecyclerViewAdapter mTopRatedAdapter;
    private TVShowsRecyclerViewAdapter mPopularAdapter;
    private TVShowsRecyclerViewAdapter mAiringTodayAdapter;

    @Bind(R.id.activity_tvshows_home_trending_recycler_view)
    RecyclerView mTrendingRecyclerView;
    @Bind(R.id.activity_tvshows_home_popular_recycler_view)
    RecyclerView mPopularRecyclerView;
    @Bind(R.id.activity_tvshows_home_airing_today_recycler_view)
    RecyclerView mAiringTodayRecyclerView;

    Context mContext;

    private LinearLayoutManager mTopRatedLinearLayoutManager;
    private LinearLayoutManager mPopularLinearLayoutManager;
    private LinearLayoutManager mAiringTodayLinearLayoutManager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tvshows_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        mQueue2 = TVListingNetworkClient.getInstance().getRequestQueue();
        mTrendingRecyclerView.setHasFixedSize(true);
        mPopularRecyclerView.setHasFixedSize(true);
        mAiringTodayRecyclerView.setHasFixedSize(true);

        mPopularLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTopRatedLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAiringTodayLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mPopularRecyclerView.setLayoutManager(mPopularLinearLayoutManager);
        mTrendingRecyclerView.setLayoutManager(mTopRatedLinearLayoutManager);
        mAiringTodayRecyclerView.setLayoutManager(mAiringTodayLinearLayoutManager);


        //popularShows
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).showsListABC(1, TVShowsHomeActivity.this);
        //TopRated
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).showsListABC(2, TVShowsHomeActivity.this);
        //AiringToday
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).showsListABC(3, TVShowsHomeActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
            ProgressBar popularProgressBar = (ProgressBar) findViewById(R.id.Activity_tvshows_home_popular_loading_progressBar);
            popularProgressBar.setVisibility(View.GONE);
            if (mPopularShows.getResults().size() == 0) {
                TextView textView = (TextView)findViewById(R.id.activity_tvshows_home_no_popular_data_text_view);
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
                TextView textView = (TextView)findViewById(R.id.activity_tvshows_home_no_popular_data_text_view);
                textView.setVisibility(View.GONE);
            }
            mPopularAdapter = new TVShowsRecyclerViewAdapter(mPopularShows, mQueue2, mContext);
            mPopularRecyclerView.setAdapter(mPopularAdapter);
        }else if (response instanceof TopRatedTVShows) {
            mTopRatedShows = ((TopRatedTVShows) response).tvShows;
            ProgressBar topRatedProgressBar = (ProgressBar) findViewById(R.id.Activity_tvshows_home_top_rated_loading_progressBar);
            topRatedProgressBar.setVisibility(View.GONE);
            if (mTopRatedShows.getResults().size() == 0) {
                TextView textView = (TextView)findViewById(R.id.activity_tvshows_home_no_top_rated_data_text_view);
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
                TextView textView = (TextView)findViewById(R.id.activity_tvshows_home_no_top_rated_data_text_view);
                textView.setVisibility(View.GONE);
            }
            mTopRatedAdapter = new TVShowsRecyclerViewAdapter(mTopRatedShows, mQueue2, mContext);
            mTrendingRecyclerView.setAdapter(mTopRatedAdapter);
        }else if (response instanceof AiringTodayTVShows) {
            mAiringTodayShows = ((AiringTodayTVShows) response).tvShows;
            ProgressBar airingTodayProgressBar = (ProgressBar) findViewById(R.id.Activity_tvshows_home_airing_today_loading_progressBar);
           /* for (int i = 0; i < likedShows.size(); i++) {
                for (int j = 0; j < mAiringTodayShows.getResults().size(); j++) {
                    if (likedShows.get(i) == mAiringTodayShows.getResults().get(j).getId()) {
                        SendNotification notification = new SendNotification();
                        notification.setNotificationData(this, mAiringTodayShows.getResults().get(j).getName()+" airs today.");
                    }
                }
            }*/
            airingTodayProgressBar.setVisibility(View.GONE);
            if (mAiringTodayShows.getResults().size() == 0) {
                TextView textView = (TextView)findViewById(R.id.activity_tvshows_home_no_airing_text_view);
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.no_data);
            }else {
                TextView textView = (TextView)findViewById(R.id.activity_tvshows_home_no_airing_text_view);
                textView.setVisibility(View.GONE);
            }
            mAiringTodayAdapter = new TVShowsRecyclerViewAdapter(mAiringTodayShows, mQueue2, mContext);
            mAiringTodayRecyclerView.setAdapter(mAiringTodayAdapter);
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }
}
