package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.tvlistings.view.callback.DisplayMovie;
import com.tvlistings.view.callback.DisplayPersonDetails;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;

/**
 * Created by Rohit on 3/9/2016.
 */
public abstract class BaseSearchActivity extends BaseListingActivity implements LoadMoreData,DisplayShow, ServiceCallbacks, DisplayPersonDetails, DisplayMovie {
    RequestQueue mQueue;
    protected String mSearch;
    @Bind(R.id.activity_base_search_search_edit_text)
    protected EditText mEditText;

    SearchResultContent mArray;

    @Bind(R.id.activity_base_search_tv_text)
    TextView mTvShowsTextView;

    @Bind(R.id.activity_base_search_discover_text)
    TextView mDiscoverTextView;

    @Bind(R.id.activity_base_search_movies_text)
    TextView mMoviesTextView;

    @Bind(R.id.activity_base_search_people_text)
    TextView mPeopleTextView;

    @Bind(R.id.activity_base_search_display_result_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.activity_base_search_progressBar)
    ProgressBar mProgressBar;

    @Bind(R.id.activity_base_search_tv_image)
    ImageView mTvImageView;

    @Bind(R.id.activity_base_search_movies_image)
    ImageView mMovieImageView;

    @Bind(R.id.activity_base_search_discover_image)
    ImageView mDiscoverImageView;

    @Bind(R.id.activity_base_search_forum_image)
    ImageView mForumImageView;

    @Bind(R.id.activity_base_search_faq_image)
    ImageView mFAQImageView;

    @Bind(R.id.activity_base_search_contact_image)
    ImageView mContactImageView;

    @Bind(R.id.activity_base_search_people_image)
    ImageView mPeopleImageView;

    @Bind(R.id.activity_base_search_no_result_text_view)
    TextView mTextView;

    SearchRecyclerViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private int mPageCount;
    private int mCurrentPage =0;

    @Bind(R.id.activity_base_search_search_result_frame_layout)
    FrameLayout mContentFrameLayout;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchRecyclerViewAdapter(mQueue, this);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);

        mTvImageView.setImageResource(R.mipmap.ic_tv_white_48dp);
        mMovieImageView.setImageResource(R.mipmap.ic_movie_white_48dp);
        mPeopleImageView.setImageResource(R.mipmap.ic_people_outline_white_48dp);
        mDiscoverImageView.setImageResource(R.mipmap.ic_search_white_48dp);
        mForumImageView.setImageResource(R.mipmap.ic_forum_white_48dp);
        mContactImageView.setImageResource(R.mipmap.ic_contact_phone_white_48dp);
        mFAQImageView.setImageResource(R.drawable.faq);

        mTvShowsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TVShowsHomeActivity.class);
                startActivity(intent);
            }
        });

        mMoviesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MoviesHomeActivity.class);
                startActivity(intent);
            }
        });

        mPeopleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonsHomeActivity.class);
                startActivity(intent);
            }
        });

        mDiscoverTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DiscoverHomeActivity.class);
                startActivity(intent);
            }
        });

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
                if (mEditText.getVisibility() == View.VISIBLE) {
                    mEditText.setVisibility(View.GONE);
                }else {
                    mEditText.setVisibility(View.VISIBLE);
                }
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
    public void displayMovie(int id) {
        Log.i("movie ID", String.valueOf(id));
        Intent intent = new Intent(this, SelectedMovieActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
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
                mTextView.setText("No Results Found");
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

    @Override
    public void displayPersonDetails(int id, String name, String poster) {
        Intent intent = new Intent(this, ShowPersonDetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("poster", poster);
        intent.putExtra("id", id);
        Log.i("sanju", "person's id" + " " + String.valueOf(id));
        startActivity(intent);
    }
}