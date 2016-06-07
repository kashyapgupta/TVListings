package mobi.wrapper.listings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.SearchService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.view.adapter.SearchRecyclerViewAdapter;
import mobi.wrapper.listings.view.callback.DisplayMovie;
import mobi.wrapper.listings.view.callback.DisplayPersonDetails;
import mobi.wrapper.listings.view.callback.DisplayShow;
import mobi.wrapper.listings.view.callback.LoadMoreData;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import butterknife.Bind;

/**
 * Created by Rohit on 3/9/2016.
 */
public abstract class BaseSearchActivity extends BaseListingActivity implements LoadMoreData,DisplayShow, ServiceCallbacks, DisplayPersonDetails, DisplayMovie {
    RequestQueue mQueue;
    protected String mSearch;
    SearchResultContent mArray;

    @Bind(R.id.activity_base_search_search_edit_text)
    protected EditText mEditText;

    @Bind(R.id.activity_base_search_search_relative_layout)
    RelativeLayout mSearchRelativeLayout;

    @Bind(R.id.activity_base_search_tv_text)
    TextView mTvShowsTextView;

    @Bind(R.id.activity_base_search_discover_text)
    TextView mDiscoverTextView;

    @Bind(R.id.activity_base_search_movies_text)
    TextView mMoviesTextView;

    @Bind(R.id.activity_base_search_people_text)
    TextView mPeopleTextView;

    @Bind(R.id.activity_base_search_toolbar)
    Toolbar mToolbar;

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

    @Bind(R.id.activity_base_search_legal_image)
    ImageView mLegalImageView;

    @Bind(R.id.activity_base_search_faq_image)
    ImageView mFAQImageView;

    @Bind(R.id.activity_base_search_contact_image)
    ImageView mContactImageView;

    @Bind(R.id.activity_base_search_people_image)
    ImageView mPeopleImageView;

    @Bind(R.id.activity_base_search_background_network_image_view)
    NetworkImageView mBackgroundAppBarImageView;

    @Bind(R.id.activity_base_search_home_image)
    ImageView mHomeImageView;

    @Bind(R.id.activity_base_search_home_text)
    TextView mHomeTextView;

    @Bind(R.id.activity_base_search_my_app_bar_layout)
    AppBarLayout mMyAppBarLayout;

    @Bind(R.id.activity_base_search_collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.activity_base_search_no_result_text_view)
    TextView mTextView;

    @Bind(R.id.activity_base_search_search_button)
    Button mSearchButton;

    @Bind(R.id.activity_base_search_search_result_frame_layout)
    FrameLayout mContentFrameLayout;

    @Bind(R.id.activity_base_search_legal_text)
    TextView mAboutUsTextView;

    SearchRecyclerViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private int mPageCount;
    private int mCurrentPage =0;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyAppBarLayout.setExpanded(false);
        mCollapsingToolbarLayout.setTitle("TVListings");
        mContext = this;
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchRecyclerViewAdapter(mQueue, this);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);

        mTvImageView.setImageResource(R.mipmap.ic_tv_white_48dp);
        mMovieImageView.setImageResource(R.mipmap.ic_movie_white_48dp);
        mPeopleImageView.setImageResource(R.mipmap.ic_people_outline_white_48dp);
        mDiscoverImageView.setImageResource(R.mipmap.ic_search_white_48dp);
        mContactImageView.setImageResource(R.mipmap.ic_contact_phone_white_48dp);
        mHomeImageView.setImageResource(R.mipmap.ic_home_white_48dp);

        mTvShowsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TVShowsHomeActivity.class);
                startActivity(intent);
            }
        });

        mAboutUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AboutUsActivity.class);
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

        mHomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        mSearchButton.setBackground(getResources().getDrawable(R.mipmap.ic_search_white_48dp));

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyAppBarLayout.setExpanded(true);
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
        Intent intent = new Intent(this, ShowActivity.class);
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
        Intent intent = new Intent(this, MovieActivity.class);
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
        Intent intent = new Intent(this, PersonDetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("poster", poster);
        intent.putExtra("id", id);
        Log.i("sanju", "person's id" + " " + String.valueOf(id));
        startActivity(intent);
    }
}