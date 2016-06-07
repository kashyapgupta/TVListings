package mobi.wrapper.listings.view.activity;

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
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.PeopleService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.PopularPeople;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.view.adapter.PopularPersonsRecyclerViewAdapter;
import mobi.wrapper.listings.view.callback.LoadMoreData;

import butterknife.Bind;

/**
 * Created by Rohit on 4/14/2016.
 */
public class PersonsHomeActivity extends BaseSearchActivity implements ServiceCallbacks {

    @Bind(R.id.activity_persons_home_popular_people_recycler_view)
    RecyclerView mPopularPeopleRecyclerView;

    @Bind(R.id.activity_persons_home_no_data_text_view)
    TextView mNoDataTextView;

    @Bind(R.id.activity_persons_home_loading_progressBar)
    ProgressBar mLoadingPrograssBar;

    RequestQueue mQueue;
    PopularPersonsRecyclerViewAdapter mPopularPersonsRecyclerViewAdapter;
    LinearLayoutManager mPopularLinearLayoutManager;
    Context mContext;
    PopularPeople mPopularPeople;
    int mColor;
    private int mCurrentPage = 0;
    private int mPageCount = 20;
    LoadMoreData mLoadMoreData;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_persons_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mLoadMoreData = this;
        mCollapsingToolbarLayout.setTitle("Popular People");
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mPopularPeopleRecyclerView.setHasFixedSize(true);
        mPopularLinearLayoutManager = new LinearLayoutManager(this);
        mPopularPeopleRecyclerView.setLayoutManager(mPopularLinearLayoutManager);

        mColor = getResources().getColor(R.color.tomato);

        mPopularPersonsRecyclerViewAdapter = new PopularPersonsRecyclerViewAdapter(mQueue, mContext, mColor);
        mPopularPeopleRecyclerView.setAdapter(mPopularPersonsRecyclerViewAdapter);

        mPopularPersonsRecyclerViewAdapter.clearData();
        ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getPopularPeople(mCurrentPage, PersonsHomeActivity.this);
    }

    @Override
    public void loadMore() {
        if (mCurrentPage < mPageCount) {
            ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getPopularPeople(mCurrentPage, PersonsHomeActivity.this);
        }
    }

    @Override
    public void displayPersonDetails(int id, String name, String poster) {
        Intent intent = new Intent(this, PersonDetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("poster", poster);
        intent.putExtra("id", id);
        Log.i("sanju", "person's id" + " " + String.valueOf(id));
        startActivity(intent);
    }

    @Override
    public void displayMovie(int id) {
        Log.i("movie ID", String.valueOf(id));
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void displayShow(int id, double rating) {
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("rating", rating);
        startActivity(intent);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }else if (response instanceof PopularPeople) {
            mPopularPeople = (PopularPeople) response;
            mLoadingPrograssBar.setVisibility(View.GONE);
            mPopularPeopleRecyclerView.setVisibility(View.VISIBLE);
            if (mCurrentPage == 0 && mPopularPeople.getResults().size() == 0) {
                mNoDataTextView.setVisibility(View.VISIBLE);
                mNoDataTextView.setText(R.string.no_data);
                mPopularPeopleRecyclerView.setVisibility(View.GONE);
            }else {
                mNoDataTextView.setVisibility(View.GONE);
                mPopularPeopleRecyclerView.setVisibility(View.VISIBLE);
            }
            mCurrentPage++;
            mPopularPersonsRecyclerViewAdapter.setData(mPopularPeople, mCurrentPage < mPageCount);
        }
    }
}
