package com.tvlistings.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.R;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.episodes.EpisodeContent;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Rohit on 3/21/2016.
 */
public class ShowEpisodeDetailsActivity extends BaseListingActivity{
    RequestQueue mQueue;
    ImageLoader mImageLoader;
    EpisodeContent mEpisode;
    @Bind(R.id.activity_show_episode_details_poster_networkimageview)
    NetworkImageView mPoster;
    @Bind(R.id.activity_show_episode_details_rating_text_view)
    TextView mRating;
    @Bind(R.id.activity_show_episode_details_votes_text_view)
    TextView mVotes;
    @Bind(R.id.activity_show_episode_details_title_text_view)
    TextView mTitle;
    @Bind(R.id.activity_show_episode_details_overview_text_view)
    TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("sanju", "in episode details activity");
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String slug = intent.getStringExtra("slug");
        Log.i("sanju", slug);
        int seasonNo = intent.getIntExtra("seasonNo", 1);
        Log.i("sanju", String.valueOf(seasonNo));
        int episodeNo = intent.getIntExtra("episodeNo", 1);
        Log.i("sanju", String.valueOf(episodeNo));
        String URLepisode = "https://api-v2launch.trakt.tv/shows/%s/seasons/%d/episodes/%d?extended=full,images";
        String urlEpisode = String.format(URLepisode, slug, seasonNo, episodeNo);
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();

        JsonObjectRequest request = new JsonObjectRequest(urlEpisode, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String reader = jsonObject.toString();
                Type listType = new TypeToken<EpisodeContent>(){}.getType();
                mEpisode = new GsonBuilder().create().fromJson(reader, listType);
                if (!mEpisode.getImages().getScreenshot().getThumb().isEmpty() && !mEpisode.getImages().getScreenshot().getThumb().equalsIgnoreCase("null")) {
                    mPoster.setImageUrl(mEpisode.getImages().getScreenshot().getThumb(), mImageLoader);
                }else {
                    mPoster.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", mImageLoader);
                }
                mTitle.setText(mEpisode.getTitle());
                mVotes.setText(mEpisode.getVotes()+" votes");
                int rating = (int) (mEpisode.getRating()*10);
                mRating.setText(rating+" %");
                mOverview.setText(mEpisode.getOverview());
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
        mQueue.add(request);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_show_episode_details;
    }
}
