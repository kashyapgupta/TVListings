package com.tvlistings.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.SearchService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.SearchRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;

/**
 * Created by Rohit on 3/9/2016.
 */
public abstract class BaseSearchActivity extends BaseListingActivity implements LoadMoreData,DisplayShow, ServiceCallbacks {
    RequestQueue mQueue;
    protected String mSearch;
    @Bind(R.id.activity_base_search_search_edit_text)
    protected EditText mEditText;
    SearchResultContent mArray;
    @Bind(R.id.activity_base_search_display_result_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.activity_base_search_progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.activity_base_search_no_result_text_view)
    TextView mTextView;
    SearchRecyclerViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private int mPageCount;
    private int mCurrentPage =0;
    @Bind(R.id.activity_base_search_search_result_frame_layout)
    FrameLayout mContentFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchRecyclerViewAdapter(mQueue, this);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearch = String.valueOf(s);
                mCurrentPage = 0;
                mProgressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mContentFrameLayout.setVisibility(View.VISIBLE);
                if (mSearch.isEmpty()) {
                    mProgressBar.setVisibility(View.GONE);
                }
                mTextView.setVisibility(View.GONE);
                mSearch = mSearch.trim();
                try {
                    mSearch = URLEncoder.encode(mSearch, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    mAdapter.clearData();
                }
                ((SearchService)TVListingServiceFactory.getInstance().getService(SearchService.class)).search(mSearch, mCurrentPage, BaseSearchActivity.this);
            }
        });
        Button button = (Button) findViewById(R.id.activity_base_search_search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base_search;
    }

    @Override
    public void loadMore() {

        if(mCurrentPage < mPageCount) {
            ((SearchService)TVListingServiceFactory.getInstance().getService(SearchService.class)).search(mSearch, mCurrentPage, BaseSearchActivity.this);
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
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base_search);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.activity_base_search_content_frame_layout);
        View view = LayoutInflater.from(this).inflate(layoutResID, contentFrameLayout, false);
        contentFrameLayout.addView(view);
    }

    @Override
    public void onBackPressed() {
        if (mContentFrameLayout.getVisibility() == View.VISIBLE) {
            mContentFrameLayout.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if(response == null) {
            mTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mTextView.setText("No ResultsFound");
        }
        if(response instanceof SearchResultContent) {
            if (((SearchResultContent)response).getResults().size() == 0) {
                mTextView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mTextView.setText("No ResultsFound");
            }else {
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mContentFrameLayout.setVisibility(View.VISIBLE);

                mArray = ((SearchResultContent)response);
                mPageCount = mArray.getTotal_pages();
                mCurrentPage++;
                mAdapter.setData(mArray, mCurrentPage < mPageCount);
            }
        }
    }
}