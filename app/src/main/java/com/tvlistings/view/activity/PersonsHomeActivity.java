package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.PeopleService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.PopularPeople;
import com.tvlistings.model.searchResult.Results;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.PopularPersonsRecyclerViewAdapter;
import com.tvlistings.view.callback.LoadMoreData;

import java.util.ArrayList;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rohit on 4/14/2016.
 */
public class PersonsHomeActivity extends BaseSearchActivity implements ServiceCallbacks {

    @Bind(R.id.activity_persons_home_popular_people_recycler_view)
    RecyclerView mPopularPeopleRecyclerView;

    @Bind(R.id.activity_persons_home_image_ciecular_image)
    CircleImageView mLoadMore;

    RequestQueue mQueue;
    PopularPersonsRecyclerViewAdapter mPopularPersonsRecyclerViewAdapter;
    LinearLayoutManager mPopularLinearLayoutManager;
    Context mContext;
    PopularPeople mPopularPeople;
    int mColor;
    private int mCurrentPage = 1;
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
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mPopularPeopleRecyclerView.setHasFixedSize(true);
        mPopularLinearLayoutManager = new LinearLayoutManager(this);
        mPopularPeopleRecyclerView.setLayoutManager(mPopularLinearLayoutManager);

        mColor = getResources().getColor(R.color.tomato);

        mPopularPersonsRecyclerViewAdapter = new PopularPersonsRecyclerViewAdapter(mQueue, mContext, mColor);
        mPopularPeopleRecyclerView.setAdapter(mPopularPersonsRecyclerViewAdapter);

        mPopularPersonsRecyclerViewAdapter.clearData();
        ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getPopularPeople(mCurrentPage, PersonsHomeActivity.this);

        mLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage < mPageCount) {
                    mLoadMoreData.loadMore();
                }else {
                    mLoadMore.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void loadMore() {
        ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getPopularPeople(mCurrentPage, PersonsHomeActivity.this);
    }

    @Override
    public void displayPersonDetails(int id, String name, String poster) {
        Intent intent = new Intent(this, ShowPersonDetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("poster", poster);
        intent.putExtra("id", id);
        Log.i("sanju", "person's id" + " " + String.valueOf(id));
        startActivity(intent);
    }

    @Override
    public void displayMovie(int id) {
        Log.i("movie ID", String.valueOf(id));
        Intent intent = new Intent(this, SelectedMovieActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
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
        if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }else if (response instanceof PopularPeople) {
            mPopularPeople = (PopularPeople) response;
            mPopularPersonsRecyclerViewAdapter.setData(mPopularPeople);
            mCurrentPage++;
        }
    }
}
