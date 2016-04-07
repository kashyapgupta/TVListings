package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.RatingImage;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.EpisodeDetailsService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.episodes.Episodes;
import com.tvlistings.view.adapter.EpisodeCrewRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayPersonDetails;

import butterknife.Bind;

/**
 * Created by Rohit on 3/21/2016.
 */
public class ShowEpisodeDetailsActivity extends BaseListingActivity implements DisplayPersonDetails, ServiceCallbacks {
    RequestQueue mQueue;
    ImageLoader mImageLoader;
    Episodes mEpisode;
    @Bind(R.id.activity_show_episode_details_poster_networkimageview)
    NetworkImageView mPoster;
    @Bind(R.id.activity_show_episode_details_heart_image_view)
    ImageView ratingImage;
    @Bind(R.id.activity_show_episode_details_rating_text_view)
    TextView mRating;
    @Bind(R.id.activity_show_episode_details_votes_text_view)
    TextView mVotes;
    @Bind(R.id.activity_show_episode_details_title_text_view)
    TextView mTitle;
    @Bind(R.id.activity_show_episode_details_overview_text_view)
    TextView mOverview;
    @Bind(R.id.activity_show_episode_details_background_networkimageview)
    NetworkImageView mBackground;
    @Bind(R.id.activity_show_episode_details_recycler_view)
    RecyclerView mEpisodeCrewRecyclerView;
    @Bind(R.id.activity_show_episode_details_Crew_text_view)
    TextView crewTextView;
    LinearLayoutManager mLinearLayoutManager;
    EpisodeCrewRecyclerViewAdapter mEpisodeCrewAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("sanju", "in episode details activity");
        mContext = this;
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 1);
        Log.i("sanju", String.valueOf(id));
        int seasonNo = intent.getIntExtra("seasonNo", 1);
        Log.i("sanju", String.valueOf(seasonNo));
        int episodeNo = intent.getIntExtra("episodeNo", 1);
        Log.i("sanju", String.valueOf(episodeNo));
        mEpisodeCrewRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mEpisodeCrewRecyclerView.setLayoutManager(mLinearLayoutManager);
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();
        ((EpisodeDetailsService) TVListingServiceFactory.getInstance().getService(EpisodeDetailsService.class)).episodeDetails(id, seasonNo, episodeNo, ShowEpisodeDetailsActivity.this);
    }

    @Override
    public void displayPersonDetails(int id, String name, String poster) {
        Intent intent = new Intent(this, ShowPersonDetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("poster", poster);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_show_episode_details;
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof Episodes) {
            mEpisode = (Episodes) response;
            String poster = String.format(UrlConstants.IMAGE_URLW_300, mEpisode.getStill_path());
            String background = String.format(UrlConstants.IMAGE_URLW_500, mEpisode.getStill_path());
            if (!mEpisode.getStill_path().isEmpty() && !mEpisode.getStill_path().equalsIgnoreCase("null")) {
                mPoster.setImageUrl(poster, mImageLoader);
                mBackground.setImageUrl(background, mImageLoader);
            } else {
                mPoster.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", mImageLoader);
            }
            mTitle.setText(mEpisode.getName());
            mVotes.setText(mEpisode.getVote_count() + " votes");
            double rating = (mEpisode.getVote_average() * 10);
            int image = RatingImage.getRatingImage(rating);
            ratingImage.setImageResource(image);
            String trimmedRating = String.format("%.1f", rating);
            mRating.setText(trimmedRating + " %");
            mOverview.setText(mEpisode.getOverview());
            if (mEpisode.getCrew().size() > 0) {
                crewTextView.setVisibility(View.VISIBLE);
            }
            mEpisodeCrewAdapter = new EpisodeCrewRecyclerViewAdapter(mEpisode, mQueue, mContext);
            mEpisodeCrewRecyclerView.setAdapter(mEpisodeCrewAdapter);
        }
    }
}
