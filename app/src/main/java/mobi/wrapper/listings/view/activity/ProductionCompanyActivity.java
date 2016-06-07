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
import mobi.wrapper.listings.controller.service.MoviesDetailsService;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.DiscoveredData;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.view.adapter.DiscoverRecyclerViewAdapter;

import butterknife.Bind;

/**
 * Created by Rohit on 4/18/2016.
 */
public class ProductionCompanyActivity extends BaseSearchActivity {

    RequestQueue mQueue;
    Context mContext;
    int mProductionId;
    DiscoveredData mMovieData;
    DiscoverRecyclerViewAdapter mMovieRecyclerViewAdapter;
    LinearLayoutManager mMovieLinearLayoutManager;

    @Bind(R.id.activity_production_company_movies_recycler_view)
    RecyclerView mMovieRecyclerView;

    @Bind(R.id.activity_production_company_no_result_text_view)
    TextView mNoResultTextView;

    @Bind(R.id.activity_production_company_loading_progressBar)
    ProgressBar mLoadingProgressbar;

    private int mCurrentPage = 0;
    private int mPageCount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_production_company;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();

        mMovieRecyclerView.setHasFixedSize(true);
        mMovieLinearLayoutManager = new LinearLayoutManager(this);
        mMovieRecyclerView.setLayoutManager(mMovieLinearLayoutManager);

        mMovieRecyclerViewAdapter = new DiscoverRecyclerViewAdapter(mQueue, mContext);
        mMovieRecyclerView.setAdapter(mMovieRecyclerViewAdapter);

        mMovieRecyclerViewAdapter.clearData();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mCollapsingToolbarLayout.setTitle(name);

        mProductionId = intent.getIntExtra("id", 0);
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).getProductionCompanyMovies(mProductionId, mCurrentPage, ProductionCompanyActivity.this);
    }

    @Override
    public void loadMore() {
        if (mCurrentPage < mPageCount) {
            ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).getProductionCompanyMovies(mProductionId, mCurrentPage, ProductionCompanyActivity.this);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }else if (response instanceof DiscoveredData) {
            mMovieData = (DiscoveredData) response;
            mPageCount = mMovieData.getTotal_pages();
            mLoadingProgressbar.setVisibility(View.GONE);
            if (mMovieData.getResults().size() > 0) {
                mMovieRecyclerView.setVisibility(View.VISIBLE);
                mCurrentPage++;
                mMovieRecyclerViewAdapter.setData(mMovieData.getResults(), mCurrentPage < mPageCount);
            }else {
                mNoResultTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
