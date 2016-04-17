package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.DiscoverService;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.DiscoveredData;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.DiscoverRecyclerViewAdapter;

import butterknife.Bind;

/**
 * Created by Rohit on 4/17/2016.
 */
public class SelectedNetworkActivity extends BaseSearchActivity {
    RequestQueue mQueue;
    Context mContext;
    DiscoverRecyclerViewAdapter mShowsRecyclerViewAdapter;
    LinearLayoutManager mShowsLinearLayoutManager;
    int mNetworkId;
    DiscoveredData mNetworkShows;

    @Bind(R.id.activity_selected_network_shows_recycler_view)
    RecyclerView mShowsRecyclerView;

    @Bind(R.id.activity_selected_network_name_text_view)
    TextView mName;
    private int mCurrentPage = 1;
    private int mPageCount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_selected_network;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mContext = this;

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mName.setText(name);
        mNetworkId = intent.getIntExtra("id", 0);

        mShowsRecyclerView.setHasFixedSize(true);
        mShowsLinearLayoutManager = new LinearLayoutManager(this);
        mShowsRecyclerView.setLayoutManager(mShowsLinearLayoutManager);

        mShowsRecyclerViewAdapter = new DiscoverRecyclerViewAdapter(mQueue, mContext);
        mShowsRecyclerView.setAdapter(mShowsRecyclerViewAdapter);

        ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getNetworkShows(mNetworkId, mCurrentPage, SelectedNetworkActivity.this);
    }

    @Override
    public void loadMore() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getNetworkShows(mNetworkId, mCurrentPage, SelectedNetworkActivity.this);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }else if (response instanceof DiscoveredData) {
            mNetworkShows = (DiscoveredData) response;
            mPageCount = mNetworkShows.getTotal_pages();
            mShowsRecyclerViewAdapter.setData(mNetworkShows.getResults());
        }
    }
}
