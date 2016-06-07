package mobi.wrapper.listings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.DiscoverService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.DiscoveredData;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.view.adapter.DiscoverRecyclerViewAdapter;
import mobi.wrapper.listings.view.callback.DisplayMovie;
import mobi.wrapper.listings.view.callback.DisplayShow;
import mobi.wrapper.listings.view.callback.LoadMoreData;

import butterknife.Bind;

/**
 * Created by Rohit on 4/20/2016.
 */
public class DiscoveredResultActivity extends BaseSearchActivity implements ServiceCallbacks, LoadMoreData, DisplayMovie, DisplayShow{

    @Bind(R.id.activity_discovered_result_recycler_view)
    RecyclerView mDiscoveredResultRecyclerView;

    @Bind(R.id.activity_discovered_results_main_relative_layout)
    RelativeLayout mMainRelativeLayout;

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
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("rating", rating);
        startActivity(intent);
    }

    @Override
    public void displayMovie(int id) {
        Log.i("movie ID", String.valueOf(id));
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
