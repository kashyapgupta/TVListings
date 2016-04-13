package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.RatingImage;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.PeopleService;
import com.tvlistings.controller.service.SeasonDetailService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.controller.service.ShowDetailsService;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.ShowContent.ShowContent;
import com.tvlistings.model.episodes.SeasonDetails;
import com.tvlistings.model.peopleCasting.PersonCasting;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.model.tvShows.TVShows;
import com.tvlistings.view.adapter.EpisodesRecyclerViewAdapter;
import com.tvlistings.view.adapter.PeopleRecyclerViewAdapter;
import com.tvlistings.view.adapter.SeasonsRecyclerViewAdapter;
import com.tvlistings.view.adapter.TVShowsRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayEpisodes;
import com.tvlistings.view.callback.DisplayPersonDetails;
import com.tvlistings.view.callback.EpisodeDetails;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Rohit on 3/10/2016.
 */

public class SelectedShowActivity extends BaseSearchActivity implements DisplayEpisodes, DisplayPersonDetails, EpisodeDetails, ServiceCallbacks{
    RequestQueue mQueue;

    @Bind(R.id.activity_selected_show_title_text_view)
    TextView mTitle;

    @Bind(R.id.activity_selected_show_original_title_text_view)
    TextView mOriginalTitle;

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

    @Bind(R.id.activity_selected_show_heart_image_view)
    ImageView ratingImage;

    ShowContent mShowData;
    double mShowRating;
    PersonCasting mPeople;
    Context mContext = this;
    int mSeasonNo;

    private SeasonsRecyclerViewAdapter mSeasonsAdapter;
    private PeopleRecyclerViewAdapter mPeopleCastAdapter;
    private PeopleRecyclerViewAdapter mPeopleCrewAdapter;
    private EpisodesRecyclerViewAdapter mEpisodesAdapter;
    private TVShowsRecyclerViewAdapter mRelatedAdapter;

    @Bind(R.id.activity_selected_show_seasons_recycler_view)
    RecyclerView mSeasonsRecyclerView;

    @Bind(R.id.activity_selected_show_people_cast_recycler_view)
    RecyclerView mPeopleCastRecyclerView;

    @Bind(R.id.activity_selected_show_people_crew_recycler_view)
    RecyclerView mPeopleCrewRecyclerView;

    @Bind(R.id.activity_selected_show_episodes_recycler_view)
    RecyclerView mEpisodesRecyclerView;

    int mId;
    SeasonDetails mEpisodes;
    TVShows mRelated;
    ImageLoader mImageLoader;

    @Bind(R.id.activity_selected_show_related_recycler_view)
    RecyclerView mRelatedRecyclerView;

    @Bind(R.id.activity_selected_show_toggle)
    ToggleButton liked;

    @Bind(R.id.activity_selected_show_like_text_view)
    TextView like;

    @Bind(R.id.activity_selected_show_unlike_text_view)
    TextView unlike;

    @Bind(R.id.activity_selected_show_status_text_view)
    TextView mStatus;

    @Bind(R.id.activity_selected_show_no_of_seasons_text_view)
    TextView mNoOfSeasons;

    @Bind(R.id.activity_selected_show_no_of_episodes_text_view)
    TextView mNoOfEpisodes;

    @Bind(R.id.activity_selected_show_language_text_view)
    TextView mLanguages;

    @Bind(R.id.activity_selected_show_first_air_date_text_view)
    TextView mFirstAirDate;

    @Bind(R.id.activity_selected_show_last_air_date_text_view)
    TextView mLastAirDate;

    @Bind(R.id.activity_selected_show_link_text_view)
    TextView mHomepage;

    @Bind(R.id.activity_selected_show_created_by_flow_layout)
    FlowLayout mFlowLayoutCreatedBy;

    ArrayList<Integer> showPreferencesList;
    SharedPreferences mSharedPreferences;
    StringBuilder likeShows = new StringBuilder();
    String likedShows;
    SharedPreferences.Editor editor;

    private LinearLayoutManager mRelatedLayoutManager;
    private LinearLayoutManager mSeasonsLayoutManager;
    private LinearLayoutManager mPeopleCastLayoutManager;
    private LinearLayoutManager mPeopleCrewLayoutManager;
    private LinearLayoutManager mEpisodesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("sanju", "in new activity");
        Intent intent = getIntent();
        mId = intent.getIntExtra("id", 1);
        mShowRating = intent.getDoubleExtra("rating", 1);
        Log.i("sanju", String.valueOf(mId));
        mSharedPreferences = getSharedPreferences("showPreferences", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        if (showPreferencesList == null) {
            showPreferencesList = new ArrayList<>();
        }

        likedShows = mSharedPreferences.getString("likedShows", "");
        String[] shows = likedShows.split(",");

        for (int i = 0; i < shows.length; i++) {
            if (!shows[i].equals("")) {
                showPreferencesList.add(Integer.valueOf(shows[i]));
            }
        }

        for (int i = 0; i < showPreferencesList.size(); i++) {
            if (mId == showPreferencesList.get(i)) {
                liked.setChecked(true);
                like.setVisibility(View.GONE);
                unlike.setVisibility(View.VISIBLE);
            }
        }

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liked.isChecked()) {
                    like.setVisibility(View.GONE);
                    unlike.setVisibility(View.VISIBLE);
                    showPreferencesList.add(mId);
                    for (int i = 0; i < showPreferencesList.size(); i++) {
                        likeShows.append(showPreferencesList.get(i)).append(",");
                    }
                    editor.putString("likedShows", likeShows.toString());
                    editor.commit();
                    Log.i("liked show", String.valueOf(mSharedPreferences.getInt("show 0", 0)));
                    Log.i("added", String.valueOf(mId));
                } else {
                    like.setVisibility(View.VISIBLE);
                    unlike.setVisibility(View.GONE);
                    for (int i = 0; i < showPreferencesList.size(); i++) {
                        if (mId == showPreferencesList.get(i)) {
                            showPreferencesList.remove(i);
                        }
                    }
                    for (int i = 0 ; i <  showPreferencesList.size(); i++) {
                        likeShows.append(showPreferencesList.get(i)).append(",");
                    }
                    editor.putString("likedShows", likeShows.toString());
                    editor.commit();
                }
            }
        });

        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mSeasonsRecyclerView.setHasFixedSize(true);
        mRelatedRecyclerView.setHasFixedSize(true);
        mPeopleCastRecyclerView.setHasFixedSize(true);
        mPeopleCrewRecyclerView.setHasFixedSize(true);
        mRelatedLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mSeasonsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPeopleCastLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPeopleCrewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRelatedRecyclerView.setLayoutManager(mRelatedLayoutManager);
        mSeasonsRecyclerView.setLayoutManager(mSeasonsLayoutManager);
        mPeopleCastRecyclerView.setLayoutManager(mPeopleCastLayoutManager);
        mPeopleCrewRecyclerView.setLayoutManager(mPeopleCrewLayoutManager);
        //Show Detail
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).getShowDetail(mId, SelectedShowActivity.this);

        //Cast
        ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getShowCast(mId, SelectedShowActivity.this);

        //Simillar Shows
        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).showsList(mId, SelectedShowActivity.this);
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_selected_show;
    }

    @Override
    public void displayEpisodes(int seasonNo, final int id) {
        mContext = this;
        mEpisodesRecyclerView.setHasFixedSize(true);
        mEpisodesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mEpisodesRecyclerView.setLayoutManager(mEpisodesLayoutManager);
        mSeasonNo = seasonNo;
        mId = id;
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();

        //Season Detail
        ((SeasonDetailService) TVListingServiceFactory.getInstance().getService(SeasonDetailService.class)).getSeasonDetail(id, seasonNo, SelectedShowActivity.this);
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
    public void episodeDetails(int id, int seasonNo, int episodeNo) {
        Intent intent = new Intent(this, ShowEpisodeDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("seasonNo", seasonNo);
        Log.i("sanju", String.valueOf(seasonNo));
        intent.putExtra("episodeNo", episodeNo);
        startActivity(intent);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof ShowContent) {
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_selected_show_frame_layout);
            FrameLayout frameLayout1 = (FrameLayout)findViewById(R.id.activity_selected_show_loading_frame_layout);
            frameLayout1.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            mShowData = (ShowContent) response;
            ArrayList<String> networks = new ArrayList<>();
            for (int i = 0; i < mShowData.getNetworks().size(); i++) {
                networks.add(mShowData.getNetworks().get(i).getName());
            }

            String networks2 = String.valueOf(networks);
            networks2 = networks2.replace("[", "");
            networks2 = networks2.replace("]", "");
/*
            //notification send
            String notificationData = (mShowData.getName()+" airs on "+ networks2);
            SendNotification notification = new SendNotification();
            notification.setNotificationData(this, notificationData);*/

            String name = mShowData.getName();
            String originalName = mShowData.getOriginal_name();
            if (name.equalsIgnoreCase(originalName)) {
                mTitle.setText(name);
            }else {
                mOriginalTitle.setVisibility(View.VISIBLE);
                mOriginalTitle.setText(originalName);
                mTitle.setTextSize(16);
                mTitle.setText("("+name+")");
            }

            if (mShowData.isIn_production()) {
                mStatus.setText("Status : "+"In production");
            }else if (!TextUtils.isEmpty(mShowData.getStatus()) && !"null".equalsIgnoreCase(mShowData.getStatus())) {
                mStatus.setText("Status : " +mShowData.getStatus());
            }else {
                mStatus.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mShowData.getFirst_air_date()) && !"null".equalsIgnoreCase(mShowData.getFirst_air_date())) {
                mFirstAirDate.setText("First air date : " + mShowData.getFirst_air_date());
            }else {
                mFirstAirDate.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mShowData.getLast_air_date()) && !"null".equalsIgnoreCase(mShowData.getLast_air_date())) {
                mLastAirDate.setText("Last air date : "+mShowData.getLast_air_date());
            }else {
                mLastAirDate.setVisibility(View.GONE);
            }
            if (mShowData.getNumber_of_seasons() > 0) {
                if (mShowData.getNumber_of_episodes() > 0) {
                    mNoOfSeasons.setText("Seasons : "+mShowData.getNumber_of_seasons());
                    mNoOfEpisodes.setText("Episodes : " +mShowData.getNumber_of_episodes());
                }else {
                    mNoOfSeasons.setText("Seasons : "+mShowData.getNumber_of_seasons());
                    mNoOfEpisodes.setVisibility(View.GONE);
                }
            }else {
                mNoOfEpisodes.setVisibility(View.GONE);
                mNoOfSeasons.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mShowData.getHomepage()) && !"null".equalsIgnoreCase(mShowData.getHomepage())) {
                TextView textView = (TextView)findViewById(R.id.activity_selected_show_homepage_text_view);
                textView.setVisibility(View.VISIBLE);
                mHomepage.setText(mShowData.getHomepage());
            }else {
                mHomepage.setVisibility(View.GONE);
            }
            TextView textView;

            if (mShowData.getCreated_by().size() > 0) {
                TextView textView1 = (TextView)findViewById(R.id.activity_selected_show_created_by_text_view);
                textView1.setVisibility(View.VISIBLE);
                for (int i = 0; i < mShowData.getCreated_by().size(); i++) {
                    textView = new TextView(this);
                    textView.setTextSize(16);
                    textView.setTextColor(getResources().getColor(R.color.tomato));
                    if (i < (mShowData.getCreated_by().size() - 1)) {
                        textView.setText(mShowData.getCreated_by().get(i).getName()+", ");
                    }else {
                        textView.setText(mShowData.getCreated_by().get(i).getName());
                    }
                    mFlowLayoutCreatedBy.addView(textView);
                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("created by id", String.valueOf(mShowData.getCreated_by().get(finalI).getId()));
                            Intent intent = new Intent(mContext, ShowPersonDetailsActivity.class);
                            intent.putExtra("name", mShowData.getCreated_by().get(finalI).getName());
                            intent.putExtra("id", mShowData.getCreated_by().get(finalI).getId());
                            intent.putExtra("poster", "null");
                            startActivity(intent);
                        }
                    });
                }
            }

            String language;
            if (mShowData.getLanguages().size() > 0) {
                language = mShowData.getLanguages().toString();
                language = language.replace("[", "");
                language = language.replace("]", "");

                mLanguages.setText("Languages : "+language);
            }else {
                mLanguages.setVisibility(View.GONE);
            }

            int votes = mShowData.getVote_count();
            mVotes.setText(String.valueOf(votes)+" votes");
            String runtime = String.valueOf(mShowData.getEpisode_run_time());
            runtime = runtime.replace("[", "");
            runtime = runtime.replace("]", "");
            mRuntime.setText("Runtime "+runtime + " mins");
            ArrayList<String> genresArray = new ArrayList<>();
            for (int i = 0; i < mShowData.getGenres().size(); i++) {
                genresArray.add(mShowData.getGenres().get(i).getName());
            }
            String genres = String.valueOf(genresArray);
            genres = genres.replace("[", "");
            genres = genres.replace("]", "");
            mGenres.setText(genres);
            mDescription.setText(mShowData.getOverview());
            String thumb = mShowData.getPoster_path();
            if (!TextUtils.isEmpty(thumb) && !"null".equals(thumb)) {
                mPoster.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mShowData.getPoster_path()), mImageLoader);
            } else {
                mPoster.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQPqJzGtMHWmE12HmhqEYZWbmulcZVb8vhUqQcHxan7DQGNrcuF", mImageLoader);
            }
            if (mShowRating != 101) {
                String trimmedRating = String.format("%.1f", mShowRating);
                int image = RatingImage.getRatingImage(mShowRating);
                ratingImage.setImageResource(image);
                mRating.setText(trimmedRating + " %");
            }else {
                double rating = (mShowData.getVote_average() * 10);
                int image = RatingImage.getRatingImage(rating);
                ratingImage.setImageResource(image);
                mRating.setText(rating + " %");
            }
            if (mShowData.getSeasons().size() > 0) {
                TextView seasonsText = (TextView)findViewById(R.id.activity_selected_show_seasons_text_view);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.Activity_selected_show_season_r_v_linear_layout);
                seasonsText.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                mSeasonsAdapter = new SeasonsRecyclerViewAdapter(mShowData, mQueue, mContext, mId);
                mSeasonsRecyclerView.setAdapter(mSeasonsAdapter);
            }
        }else if (response instanceof PersonCasting) {
            mPeople = (PersonCasting) response;
            if (mPeople.getCast().size() > 0) {
                TextView textView = (TextView)findViewById(R.id.activity_selected_show_people_text_view);
                TextView textView1 = (TextView)findViewById(R.id.activity_selected_show_people_cast_text_view);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.Activity_selected_show_people_cast_r_v_linear_layout);
                textView.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                mPeopleCastAdapter = new PeopleRecyclerViewAdapter(mPeople, mQueue, mContext, true);
                mPeopleCastRecyclerView.setAdapter(mPeopleCastAdapter);
            }
            if (mPeople.getCrew().size() > 0) {

                TextView textView = (TextView)findViewById(R.id.activity_selected_show_people_text_view);
                TextView textView1 = (TextView)findViewById(R.id.activity_selected_show_people_crew_text_view);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.Activity_selected_show_people_crew_r_v_linear_layout);
                textView.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                mPeopleCrewAdapter = new PeopleRecyclerViewAdapter(mPeople, mQueue, mContext, false);
                mPeopleCrewRecyclerView.setAdapter(mPeopleCrewAdapter);
            }
        }else if (response instanceof TVShows) {
            mRelated = (TVShows) response;
            if (mRelated.getResults().size() > 0) {
                TextView textView = (TextView)findViewById(R.id.activity_selected_show_related_text_view);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.Activity_selected_show_related_r_v_linear_layout);
                textView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                mRelatedAdapter = new TVShowsRecyclerViewAdapter(mRelated, mQueue, mContext);
                mRelatedRecyclerView.setAdapter(mRelatedAdapter);
            }
        }else if (response instanceof SeasonDetails) {
            mEpisodes = (SeasonDetails) response;
            if (mEpisodes.getEpisodes().size() > 0) {
                TextView episodes = (TextView)findViewById(R.id.activity_selected_show_episodes_text_view);
                episodes.setVisibility(View.VISIBLE);
                LinearLayout episodeLV = (LinearLayout)findViewById(R.id.Activity_selected_show_episodes_r_v_linear_layout);
                episodeLV.setVisibility(View.VISIBLE);
            }
            mEpisodesAdapter = new EpisodesRecyclerViewAdapter(mEpisodes, mQueue, mId, mSeasonNo, mContext);
            mEpisodesRecyclerView.setAdapter(mEpisodesAdapter);
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }
}