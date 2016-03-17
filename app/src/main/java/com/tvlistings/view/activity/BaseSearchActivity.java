package com.tvlistings.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.R;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.SearchResultContent;
import com.tvlistings.view.adapter.SearchRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Rohit on 3/9/2016.
 */
public abstract class BaseSearchActivity extends BaseListingActivity implements LoadMoreData,DisplayShow {
    RequestQueue mQueue;
    public static final String URL = "https://api-v2launch.trakt.tv/search?query=%s&type=show&page=%d&limit=10";
    protected String mSearch;
    @Bind(R.id.activity_base_search_search_edit_text)
    protected EditText mEditText;
    ArrayList<SearchResultContent> mArray;
    @Bind(R.id.activity_base_search_display_result_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.activity_base_search_progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.activity_base_search_no_result_text_view)
    TextView mTextView;
    SearchRecyclerViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String mUrl;
    private int mPageCount;
    private int mCurrentPage =0;
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
        mArray = new ArrayList<>();
        mContentFrameLayout = (FrameLayout) findViewById(R.id.activity_base_search_search_result_frame_layout);
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
                mSearch.trim();
                try {
                    mSearch = URLEncoder.encode(mSearch, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mUrl = String.format(URL, mSearch, mCurrentPage + 1);
                getData(true);
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

    private void getData(final boolean newData) {
        mArray.clear();
        JsonArrayRequest req = new JsonArrayRequest(mUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("sanju", "in response");


                if (response.length() == 0) {
                    mTextView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mTextView.setText("No ResultsFound");
                }else {
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mContentFrameLayout.setVisibility(View.VISIBLE);
                }
                String reader = response.toString();
                Type listType = new TypeToken<ArrayList<SearchResultContent>>(){}.getType();
                mArray = new GsonBuilder().create().fromJson(reader, listType);
                mCurrentPage++;
                mAdapter.setData(mArray, mCurrentPage < mPageCount, newData);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("sanju", "in error response");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.i("sanju","in get headers");
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("trakt-api-version", "2");
                headers.put("trakt-api-key", "4f6cce7cd051fec2bed645fcd529b923320d91119785a187b3773f3083ff9e32");
                return headers;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                if(response.headers != null) {
                    String pageCount = response.headers.get("X-Pagination-Page-Count");
                    if(!TextUtils.isEmpty(pageCount)) {
                        mPageCount = Integer.parseInt(pageCount);
                    }
                }
                return super.parseNetworkResponse(response);
            }
        };
        mQueue.add(req);
    }

    @Override
    public void loadMore() {

        if(mCurrentPage < mPageCount) {
            mUrl = String.format(URL, mSearch, mCurrentPage + 1);
            getData(false);
        }
    }

    @Override
    public void displayShow(String slug) {
        Intent intent = new Intent(this, SelectedShowActivity.class);
        intent.putExtra("slug", slug);
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
}