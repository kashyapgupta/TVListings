package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tvlistings.model.PersonsCasting;
import com.tvlistings.view.adapter.PersonCastingShowsAdapter;
import com.tvlistings.view.callback.DisplayShow;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Rohit on 3/18/2016.
 */
public class ShowPersonDetailsActivity extends BaseListingActivity implements DisplayShow{
    String URLshows = "https://api-v2launch.trakt.tv/people/%s/shows?extended=full,images";
    String mName;
    String mHeadshot;
    String mFanart;
    String mBirthday;
    String mBiography;
    String mBirthplace;
    String mSlug;
    @Bind(R.id.activity_show_person_details_headshot_networkimageview)
    NetworkImageView mHeadshotImage;
    @Bind(R.id.activity_show_person_details_fanart_networkimageview)
    NetworkImageView mFanartImage;
    @Bind(R.id.activity_show_person_details_name_text_view)
    TextView mNameText;
    @Bind(R.id.activity_show_person_details_born_text_view)
    TextView mBirthdayText;
    @Bind(R.id.activity_show_person_details_biography_text_view)
    TextView mBiographyText;
    ImageLoader mImageLoader;
    RequestQueue mQueue;
    PersonsCasting mPersonsCastingShows;
    @Bind(R.id.activity_show_person_details_casting_shows_recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonCastingShowsAdapter mPersonCastingShowAdapter;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Intent intent = getIntent();
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();
        mName = intent.getStringExtra("name");
        mHeadshot = intent.getStringExtra("headshot");
        if (mHeadshot.equalsIgnoreCase("null") || mHeadshot.isEmpty()) {
            mHeadshot = "https://www.irbbarcelona.org/sites/default/files/default_images/people.png";
        }
        mFanart = intent.getStringExtra("fanart");
        if (mFanart.equalsIgnoreCase("null") || mFanart.isEmpty()) {
            mFanart = "https://www.planwallpaper.com/static/images/7019370-artistic-desktop-backgrounds-images.jpg";
        }
        mBirthday = intent.getStringExtra("birthday");
        mBiography = intent.getStringExtra("biography");
        mBirthplace = intent.getStringExtra("birthplace");
        mSlug = intent.getStringExtra("slug");
        mFanartImage.setImageUrl(mFanart, mImageLoader);
        mHeadshotImage.setImageUrl(mHeadshot, mImageLoader);
        mNameText.setText(mName);
        if (mBirthday.equalsIgnoreCase("null") || mBirthday.isEmpty()) {
            if (mBirthplace.equalsIgnoreCase("null") || mBirthplace.isEmpty()) {
                mBirthdayText.setText("");
            }else {
                mBirthdayText.setText("BORN"+" in " + mBirthplace);
            }
        }else {
            if (mBirthplace.equalsIgnoreCase("null") || mBirthplace.isEmpty()) {
                mBirthdayText.setText("BORN "+ mBirthday);
            }else {
                mBirthdayText.setText("BORN " + mBirthday + " in " + mBirthplace);
            }
        }
        mBiographyText.setText(mBiography);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String url = String.format(URLshows, mSlug);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String reader = jsonObject.toString();
                Type listType = new TypeToken<PersonsCasting>(){}.getType();
                mPersonsCastingShows = new GsonBuilder().create().fromJson(reader, listType);
                mPersonCastingShowAdapter = new PersonCastingShowsAdapter(mPersonsCastingShows, mQueue, mContext);
                mRecyclerView.setAdapter(mPersonCastingShowAdapter);
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
        return R.layout.activity_show_person_details;
    }
    @Override
    public void displayShow(String slug) {
        Intent intent = new Intent(this, SelectedShowActivity.class);
        intent.putExtra("slug", slug);
        startActivity(intent);
    }
}
