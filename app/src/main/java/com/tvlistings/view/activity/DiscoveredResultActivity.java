package com.tvlistings.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.DiscoverService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.DiscoveredData;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.DiscoverRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayMovie;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rohit on 4/20/2016.
 */
public class DiscoveredResultActivity extends BaseSearchActivity implements ServiceCallbacks, LoadMoreData, DisplayMovie, DisplayShow{

    @Bind(R.id.activity_discovered_result_recycler_view)
    RecyclerView mDiscoveredResultRecyclerView;

    @Bind(R.id.activity_discovered_results_loading_progressBar)
    ProgressBar mLoadingProgressBar;

    @Bind(R.id.activity_discovered_results_text_view)
    TextView mNoResultTextView;

    DiscoverRecyclerViewAdapter mDiscoveredRecyclerViewAdapter;
    LinearLayoutManager mDiscoveredLinearLayoutManager;

    private Context mContext;
    RequestQueue mQueue;
    String mUrl;
    DiscoveredData mDiscoveredData;
    int mCurrentPage = 0;
    int mPageCount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_discovered_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");

        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mContext = this;

        mDiscoveredResultRecyclerView.setHasFixedSize(true);
        mDiscoveredLinearLayoutManager = new LinearLayoutManager(this);
        mDiscoveredResultRecyclerView.setLayoutManager(mDiscoveredLinearLayoutManager);
        mDiscoveredRecyclerViewAdapter = new DiscoverRecyclerViewAdapter(mQueue, mContext);
        mDiscoveredResultRecyclerView.setAdapter(mDiscoveredRecyclerViewAdapter);

        mDiscoveredRecyclerViewAdapter.clearData();

        String url = mUrl+"&page=%d";
        ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getDiscoveredData(url, mCurrentPage, DiscoveredResultActivity.this);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof DiscoveredData) {
            mDiscoveredData = (DiscoveredData) response;
            mLoadingProgressBar.setVisibility(View.GONE);
            mPageCount = mDiscoveredData.getTotal_pages();
            if (mDiscoveredData.getResults().size() > 0) {
                mDiscoveredResultRecyclerView.setVisibility(View.VISIBLE);
                mCurrentPage++;
                mDiscoveredRecyclerViewAdapter.setData(mDiscoveredData.getResults(), mCurrentPage < mPageCount);
            }else {
                mNoResultTextView.setVisibility(View.VISIBLE);
            }
        } else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }

    @Override
    public void loadMore() {
        if (mCurrentPage < mPageCount) {
            String url = mUrl+"&page=%d";
            ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getDiscoveredData(url, mCurrentPage, DiscoveredResultActivity.this);
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
    public void displayMovie(int id) {
        Log.i("movie ID", String.valueOf(id));
        Intent intent = new Intent(this, SelectedMovieActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
