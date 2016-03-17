package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.R;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.Show;
import com.tvlistings.model.trending.TrendingShows;
import com.tvlistings.view.adapter.PopularRecyclerViewAdapter;
import com.tvlistings.view.adapter.TrendingRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayShow;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Rohit on 3/14/2016.
 */
public class HomeActivity extends BaseSearchActivity implements DisplayShow{

    private String URLtrending = "https://api-v2launch.trakt.tv/shows/trending?extended=full,images";
    private String URLpopular = "https://api-v2launch.trakt.tv/shows/popular?extended=full,images";
    private RequestQueue mQueue2;
    private ArrayList<TrendingShows> mTrendingShows;
    private ArrayList<Show> mPopularShows;
    private TrendingRecyclerViewAdapter mTrendingAdapter;
    private PopularRecyclerViewAdapter mPopularAdapter;
    @Bind(R.id.activity_home_trending_recycler_view)
    RecyclerView mTrendingRecyclerView;
    @Bind(R.id.activity_home_popular_recycler_view)
    RecyclerView mPopularRecyclerView;
    Context mContext;

    private LinearLayoutManager mTrendingLinearLayoutManager;
    private LinearLayoutManager mPopularLinearLayoutManager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        mTrendingShows = new ArrayList<>();
        mQueue2 = TVListingNetworkClient.getInstance().getRequestQueue();
        mTrendingRecyclerView.setHasFixedSize(true);
        mPopularRecyclerView.setHasFixedSize(true);
        mPopularLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrendingLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPopularRecyclerView.setLayoutManager(mPopularLinearLayoutManager);
        mTrendingRecyclerView.setLayoutManager(mTrendingLinearLayoutManager);
        JsonArrayRequest request = new JsonArrayRequest(URLtrending, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                String reader = jsonArray.toString();
                Type listType = new TypeToken<ArrayList<TrendingShows>>(){}.getType();
                mTrendingShows = new GsonBuilder().create().fromJson(reader, listType);
                mTrendingAdapter = new TrendingRecyclerViewAdapter(mTrendingShows, mQueue2, mContext);
                mTrendingRecyclerView.setAdapter(mTrendingAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("trakt-api-key","4f6cce7cd051fec2bed645fcd529b923320d91119785a187b3773f3083ff9e32");
                headers.put("trakt-api-version","2");
                return headers;
            }
        };
        mQueue2.add(request);

        JsonArrayRequest request1 = new JsonArrayRequest(URLpopular, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                String reader = jsonArray.toString();
                Type listType = new TypeToken<ArrayList<Show>>(){}.getType();
                mPopularShows = new GsonBuilder().create().fromJson(reader, listType);
                mPopularAdapter = new PopularRecyclerViewAdapter(mPopularShows, mQueue2, mContext);
                mPopularRecyclerView.setAdapter(mPopularAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("trakt-api-key","4f6cce7cd051fec2bed645fcd529b923320d91119785a187b3773f3083ff9e32");
                headers.put("trakt-api-version","2");
                return headers;
            }
        };
        mQueue2.add(request1);
    }

    @Override
    public void displayShow(String slug) {
        Intent intent = new Intent(this, SelectedShowActivity.class);
        intent.putExtra("slug", slug);
        startActivity(intent);
    }

}
