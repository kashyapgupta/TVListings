package mobi.wrapper.listings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.DiscoverService;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.DiscoveredData;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.view.adapter.DiscoverRecyclerViewAdapter;

import butterknife.Bind;

/**
 * Created by Rohit on 4/17/2016.
 */
public class NetworkActivity extends BaseSearchActivity {
    RequestQueue mQueue;
    Context mContext;
    DiscoverRecyclerViewAdapter mShowsRecyclerViewAdapter;
    LinearLayoutManager mShowsLinearLayoutManager;
    int mNetworkId;
    DiscoveredData mNetworkShows;

    @Bind(R.id.activity_selected_network_shows_recycler_view)
    RecyclerView mShowsRecyclerView;

    @Bind(R.id.activity_selected_network_loading_progressBar)
    ProgressBar mLoadingProgressBar;

    @Bind(R.id.activity_selected_network_no_result_text_view)
    TextView mNoResultTextView;

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
        mCollapsingToolbarLayout.setTitle(name);
        mNetworkId = intent.getIntExtra("id", 0);

        mShowsRecyclerView.setHasFixedSize(true);
        mShowsLinearLayoutManager = new LinearLayoutManager(this);
        mShowsRecyclerView.setLayoutManager(mShowsLinearLayoutManager);

        mShowsRecyclerViewAdapter = new DiscoverRecyclerViewAdapter(mQueue, mContext);
        mShowsRecyclerView.setAdapter(mShowsRecyclerViewAdapter);
        mShowsRecyclerViewAdapter.clearData();

        ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getNetworkShows(mNetworkId, mCurrentPage, NetworkActivity.this);
    }

    @Override
    public void loadMore() {
        if (mCurrentPage < mPageCount) {
            ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getNetworkShows(mNetworkId, mCurrentPage, NetworkActivity.this);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }else if (response instanceof DiscoveredData) {
            mNetworkShows = (DiscoveredData) response;
            mPageCount = mNetworkShows.getTotal_pages();
            mLoadingProgressBar.setVisibility(View.GONE);
            if (mNetworkShows.getResults().size() > 0) {
                mShowsRecyclerView.setVisibility(View.VISIBLE);
                mCurrentPage++;
                mShowsRecyclerViewAdapter.setData(mNetworkShows.getResults(), mCurrentPage < mPageCount);
            }else {
                mNoResultTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
