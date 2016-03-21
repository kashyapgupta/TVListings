package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.R;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.PerticularShowContents;
import com.tvlistings.model.SeasonContent;
import com.tvlistings.model.Show;
import com.tvlistings.model.episodes.EpisodeContent;
import com.tvlistings.model.people.People;
import com.tvlistings.view.adapter.EpisodesRecyclerViewAdapter;
import com.tvlistings.view.adapter.PeopleRecyclerViewAdapter;
import com.tvlistings.view.adapter.PopularRecyclerViewAdapter;
import com.tvlistings.view.adapter.SeasonsRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayEpisodes;
import com.tvlistings.view.callback.DisplayPersonDetails;
import com.tvlistings.view.callback.EpisodeDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Rohit on 3/10/2016.
 */

public class SelectedShowActivity extends BaseSearchActivity implements DisplayEpisodes, DisplayPersonDetails, EpisodeDetails{
    protected String URL2 = "https://api-v2launch.trakt.tv/shows/%s?extended=full,images";
    RequestQueue mQueue;
    @Bind(R.id.activity_selected_show_title_text_view)
    TextView mTitle;
    @Bind(R.id.activity_selected_show_votes_text_view)
    TextView mVotes;
    @Bind(R.id.activity_selected_show_runtime_text_view)
    TextView mRuntime;
    @Bind(R.id.activity_selected_show_genres_text_view)
    TextView mGenres;
    @Bind(R.id.activity_selected_show_description_text_view)
    TextView mDescription;
    @Bind(R.id.activity_selected_show_poster_networkimageview)
    NetworkImageView mPoster;
    @Bind(R.id.activity_selected_show_rating_text_view)
    TextView mRating;
    PerticularShowContents mShowData;
    People mPeople;
    Context mContext;
    int mSeasonNo;
    private SeasonsRecyclerViewAdapter mSeasonsAdapter;
    private PeopleRecyclerViewAdapter mPeopleAdapter;
    private EpisodesRecyclerViewAdapter mEpisodesAdapter;
    private PopularRecyclerViewAdapter mRelatedAdapter;
    @Bind(R.id.activity_selected_show_seasons_recycler_view)
    RecyclerView mSeasonsRecyclerView;
    @Bind(R.id.activity_selected_show_people_recycler_view)
    RecyclerView mPeoplesRecyclerView;
    @Bind(R.id.activity_selected_show_episodes_recycler_view)
    RecyclerView mEpisodesRecyclerView;
    String mSlug;
    ArrayList<SeasonContent> mSeasons;
    ArrayList<EpisodeContent> mEpisodes;
    private ArrayList<Show> mRelated;
    @Bind(R.id.activity_selected_show_related_recycler_view)
    RecyclerView mRelatedRecyclerView;
    ImageLoader mImageLoader;
    private RecyclerView.LayoutManager mRelatedLayoutManager;
    private RecyclerView.LayoutManager mSeasonsLayoutManager;
    private RecyclerView.LayoutManager mPeopleLayoutManager;
    private RecyclerView.LayoutManager mEpisodesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("sanju", "in new activity");
        mContext = this;
        Intent intent = getIntent();
        mSlug = intent.getStringExtra("slug");
        Log.i("sanju", mSlug);
        String url2 = String.format(URL2, mSlug);
        Log.i("sanju", url2);
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mSeasonsRecyclerView.setHasFixedSize(true);
        mRelatedRecyclerView.setHasFixedSize(true);
        mPeoplesRecyclerView.setHasFixedSize(true);
        mRelatedLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mSeasonsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPeopleLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRelatedRecyclerView.setLayoutManager(mRelatedLayoutManager);
        mSeasonsRecyclerView.setLayoutManager(mSeasonsLayoutManager);
        mPeoplesRecyclerView.setLayoutManager(mPeopleLayoutManager);
        mSeasons = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("sanju", "in new activity's on response");
                String reader = response.toString();
                Type listType = new TypeToken<PerticularShowContents>(){}.getType();
                mShowData = new GsonBuilder().create().fromJson(reader, listType);
                mTitle.setText(mShowData.getTitle());
                int votes = (int) mShowData.getVotes();
                Log.i("sanju", String.valueOf(votes));
                mVotes.setText(String.valueOf(votes));
                mRuntime.setText(mShowData.getRuntime() + " mins");
                ArrayList<String> genresArray = mShowData.getGenres();
                String genres = String.valueOf(genresArray);
                genres = genres.replace("[", "");
                genres = genres.replace("]", "");
                mGenres.setText(genres);
                mDescription.setText(mShowData.getOverview());
                String thumb = mShowData.getImages().getPoster().getThumb();
                if(!TextUtils.isEmpty(thumb) && !"null".equals(thumb)) {
                    mPoster.setImageUrl(thumb, mImageLoader);
                }else {
                    mPoster.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQPqJzGtMHWmE12HmhqEYZWbmulcZVb8vhUqQcHxan7DQGNrcuF", mImageLoader);
                }
                int rating = (int)(mShowData.getRating()*10);
                mRating.setText(String.valueOf(rating));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("trakt-api-key","4f6cce7cd051fec2bed645fcd529b923320d91119785a187b3773f3083ff9e32");
                headers.put("trakt-api-version","2");
                return headers;
            }

        };
        mQueue.add(request);
        String URL = "https://api-v2launch.trakt.tv/shows/%s/seasons?extended=full,images";
        String url = String.format(URL, mSlug);
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("sanju", "in seasons on response");
                String reader = response.toString();
                Type listType = new TypeToken<ArrayList<SeasonContent>>(){}.getType();
                mSeasons = new GsonBuilder().create().fromJson(reader, listType);
                mSeasonsAdapter = new SeasonsRecyclerViewAdapter(mSeasons, mQueue, mContext, mSlug);
                mSeasonsRecyclerView.setAdapter(mSeasonsAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("trakt-api-version", "2");
                headers.put("trakt-api-key", "4f6cce7cd051fec2bed645fcd529b923320d91119785a187b3773f3083ff9e32");
                return headers;
            }
        };
        mQueue.add(req);
        String URL3 = "https://api-v2launch.trakt.tv/shows/%s/people?extended=full,images";
        String url3 = String.format(URL3, mSlug);
        JsonObjectRequest request1 = new JsonObjectRequest(url3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String reader = jsonObject.toString();
                Type listType = new TypeToken<People>(){}.getType();
                mPeople = new GsonBuilder().create().fromJson(reader, listType);
                mPeopleAdapter = new PeopleRecyclerViewAdapter(mPeople, mQueue, mContext);
                mPeoplesRecyclerView.setAdapter(mPeopleAdapter);
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
        mQueue.add(request1);
        String URLrelated = "https://api-v2launch.trakt.tv/shows/%s/related?extended=full,images";
        String url4 = String.format(URLrelated, mSlug);
        JsonArrayRequest request2 = new JsonArrayRequest(url4, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                String reader = jsonArray.toString();
                Type listType = new TypeToken<ArrayList<Show>>(){}.getType();
                mRelated = new GsonBuilder().create().fromJson(reader, listType);
                mRelatedAdapter = new PopularRecyclerViewAdapter(mRelated, mQueue, mContext);
                mRelatedRecyclerView.setAdapter(mRelatedAdapter);
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
        mQueue.add(request2);
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_selected_show;
    }

    @Override
    public void displayEpisodes(int seasonNo, String slug) {
        TextView episodes = (TextView)findViewById(R.id.activity_selected_show_episodes_text_view);
        episodes.setVisibility(View.VISIBLE);
        LinearLayout episodeLV = (LinearLayout)findViewById(R.id.Activity_selected_show_episodes_r_v_linear_layout);
        episodeLV.setVisibility(View.VISIBLE);
        mEpisodes = new ArrayList<>();
        mContext = this;
        mEpisodesRecyclerView.setHasFixedSize(true);
        mEpisodesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mEpisodesRecyclerView.setLayoutManager(mEpisodesLayoutManager);
        String URL = "https://api-v2launch.trakt.tv/shows/%s/seasons/%d/episodes?extended=full,images";
        String url = String.format(URL, slug, seasonNo);
        mSlug = slug;
        mSeasonNo = seasonNo;
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        JsonArrayRequest req1 = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                String reader = jsonArray.toString();
                Type listType = new TypeToken<ArrayList<EpisodeContent>>(){}.getType();
                mEpisodes = new GsonBuilder().create().fromJson(reader, listType);
                mEpisodesAdapter = new EpisodesRecyclerViewAdapter(mEpisodes, mQueue, mSlug, mSeasonNo, mContext);
                mEpisodesRecyclerView.setAdapter(mEpisodesAdapter);
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
        mQueue.add(req1);
    }

    @Override
    public void displayPersonDetails(String name, String headshot, String fanart, String biography, String birthday, String birthplace, String slug) {
        Intent intent = new Intent(this, ShowPersonDetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("headshot", headshot);
        intent.putExtra("fanart", fanart);
        intent.putExtra("biography", biography);
        intent.putExtra("birthday", birthday);
        intent.putExtra("birthplace", birthplace);
        intent.putExtra("slug", slug);
        startActivity(intent);
    }

    @Override
    public void episodeDetails(String slug, int seasonNo, int episodeNo) {
        Intent intent = new Intent(this, ShowEpisodeDetailsActivity.class);
        intent.putExtra("slug", slug);
        intent.putExtra("seasonNo", seasonNo);
        intent.putExtra("episodeNo", episodeNo);
        startActivity(intent);
    }
}