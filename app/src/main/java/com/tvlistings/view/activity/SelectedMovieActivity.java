package com.tvlistings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.tvlistings.controller.service.MoviesDetailsService;
import com.tvlistings.controller.service.PeopleService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.movieContents.MovieContent;
import com.tvlistings.model.moviesList.MoviesList;
import com.tvlistings.model.peopleCasting.PersonCasting;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.MoviesRecyclerViewAdapter;
import com.tvlistings.view.adapter.PeopleRecyclerViewAdapter;
import com.tvlistings.view.callback.DisplayPersonDetails;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Rohit on 4/8/2016.
 */
public class SelectedMovieActivity extends BaseSearchActivity implements ServiceCallbacks, DisplayPersonDetails {
    RequestQueue mQueue;
    ImageLoader mImageLoader;
    PersonCasting mPeople;
    MovieContent mMovieData;
    MoviesList mRelatedMovies;

    @Bind(R.id.activity_selected_movie_title_text_view)
    TextView mTitle;

    @Bind(R.id.activity_selected_movie_original_title_text_view)
    TextView mOriginalTitle;

    @Bind(R.id.activity_selected_movie_genres_text_view)
    TextView mGenres;

    @Bind(R.id.activity_selected_movie_toggle)
    ToggleButton mLikeMovie;

    @Bind(R.id.activity_selected_movie_like_text_view)
    TextView mLike;

    @Bind(R.id.activity_selected_movie_unlike_text_view)
    TextView mUnlike;

    @Bind(R.id.activity_selected_movie_rating_text_view)
    TextView mRating;

    @Bind(R.id.activity_selected_movie_votes_text_view)
    TextView mVotes;

    @Bind(R.id.activity_selected_movie_description_text_view)
    TextView mOverview;

    @Bind(R.id.activity_selected_movie_tagline_text_view)
    TextView mTagline;

    @Bind(R.id.activity_selected_movie_collection_text_view)
    TextView mCollection;

    @Bind(R.id.activity_selected_movie_language_spoken_text_view)
    TextView mLanguage;

    @Bind(R.id.activity_selected_movie_link_text_view)
    TextView mHomepage;

    @Bind(R.id.activity_selected_movie_release_date_text_view)
    TextView mReleaseDate;

    @Bind(R.id.activity_selected_movie_runtime_text_view)
    TextView mRuntime;

    @Bind(R.id.activity_selected_movie_budget_text_view)
    TextView mBudget;

    @Bind(R.id.activity_selected_movie_revenue_text_view)
    TextView mRevenue;

    @Bind(R.id.activity_selected_movie_poster_networkimageview)
    NetworkImageView mPoster;

    @Bind(R.id.activity_selected_movie_heart_image_view)
    ImageView mRatingImage;

    @Bind(R.id.activity_selected_movie_people_cast_recycler_view)
    RecyclerView mPeopleCastRecyclerview;

    @Bind(R.id.activity_selected_movie_people_crew_recycler_view)
    RecyclerView mPeopleCrewRecyclerview;

    @Bind(R.id.activity_selected_movie_related_recycler_view)
    RecyclerView mRelatedMoviesRecyclerView;

    LinearLayoutManager mPeopleCastLinearLayoutManager;
    LinearLayoutManager mPeopleCrewLinearLayoutManager;
    LinearLayoutManager mRelatedMoviesLinearLayoutManager;

    PeopleRecyclerViewAdapter mPeopleCastAdapter;
    PeopleRecyclerViewAdapter mPeopleCrewAdapter;
    MoviesRecyclerViewAdapter mRelatedMoviesAdapter;

    StringBuilder likeMovies = new StringBuilder();
    String likedMovies;
    ArrayList<Integer> moviesPreferencesList;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    int mMovieId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mMovieId = intent.getIntExtra("id", 0);

        mSharedPreferences = getSharedPreferences("moviesPreferences", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        if (moviesPreferencesList == null) {
            moviesPreferencesList = new ArrayList<>();
        }

        likedMovies = mSharedPreferences.getString("likedMovies", "");
        String[] shows = likedMovies.split(",");

        for (int i = 0; i < shows.length; i++) {
            if (!shows[i].equals("")) {
                moviesPreferencesList.add(Integer.valueOf(shows[i]));
            }
        }

        for (int i = 0; i < moviesPreferencesList.size(); i++) {
            if (mMovieId == moviesPreferencesList.get(i)) {
                mLikeMovie.setChecked(true);
                mLike.setVisibility(View.GONE);
                mUnlike.setVisibility(View.VISIBLE);
            }
        }

        mLikeMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLikeMovie.isChecked()) {
                    mLike.setVisibility(View.GONE);
                    mUnlike.setVisibility(View.VISIBLE);
                    moviesPreferencesList.add(mMovieId);
                    for (int i = 0; i < moviesPreferencesList.size(); i++) {
                        likeMovies.append(moviesPreferencesList.get(i)).append(",");
                    }
                    editor.putString("likedMovies", likeMovies.toString());
                    editor.commit();
                    Log.i("liked show", String.valueOf(mSharedPreferences.getInt("show 0", 0)));
                    Log.i("added", String.valueOf(mMovieId));
                } else {
                    mLike.setVisibility(View.VISIBLE);
                    mUnlike.setVisibility(View.GONE);
                    for (int i = 0; i < moviesPreferencesList.size(); i++) {
                        if (mMovieId == moviesPreferencesList.get(i)) {
                            moviesPreferencesList.remove(i);
                        }
                    }
                    for (int i = 0; i < moviesPreferencesList.size(); i++) {
                        likeMovies.append(moviesPreferencesList.get(i)).append(",");
                    }
                    editor.putString("likedMovies", likeMovies.toString());
                    editor.commit();
                }
            }
        });

        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();

        mPeopleCastRecyclerview.setHasFixedSize(true);
        mPeopleCastLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPeopleCastRecyclerview.setLayoutManager(mPeopleCastLinearLayoutManager);

        mPeopleCrewRecyclerview.setHasFixedSize(true);
        mPeopleCrewLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPeopleCrewRecyclerview.setLayoutManager(mPeopleCrewLinearLayoutManager);

        mRelatedMoviesRecyclerView.setHasFixedSize(true);
        mRelatedMoviesLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRelatedMoviesRecyclerView.setLayoutManager(mRelatedMoviesLinearLayoutManager);


        //MovieDetails
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).getMovieDetail(mMovieId, SelectedMovieActivity.this);

        //Get Cast
        ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getMovieCast(mMovieId, SelectedMovieActivity.this);

        //Similar Movies
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).moviesList(mMovieId, SelectedMovieActivity.this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_selected_movie;
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
    public void onSuccess(BaseResponse response) {
        if (response instanceof MovieContent) {
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_selected_movie_frame_layout);
            FrameLayout frameLayout1 = (FrameLayout)findViewById(R.id.activity_selected_movie_loading_frame_layout);
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout1.setVisibility(View.GONE);
            mMovieData = (MovieContent) response;

            String title = mMovieData.getTitle();
            String originalTitle = mMovieData.getOriginal_title();
            if (title.equalsIgnoreCase(originalTitle)) {
                mTitle.setText(title);
            }else {
                mOriginalTitle.setVisibility(View.VISIBLE);
                mOriginalTitle.setText(originalTitle);
                mTitle.setTextSize(16);
                mTitle.setText("("+title+")");
            }
            int votes = mMovieData.getVote_count();
            mVotes.setText(String.valueOf(votes)+" votes");
            String runtime = String.valueOf(mMovieData.getRuntime());
            runtime = runtime.replace("[", "");
            runtime = runtime.replace("]", "");
            if (Integer.valueOf(runtime) != 0) {
                mRuntime.setVisibility(View.VISIBLE);
                mRuntime.setText("Runtime: " + runtime + " mins");
            }
            ArrayList<String> genresArray = new ArrayList<>();
            for (int i = 0; i < mMovieData.getGenres().size(); i++) {
                genresArray.add(mMovieData.getGenres().get(i).getName());
            }
            String genres = String.valueOf(genresArray);
            genres = genres.replace("[", "");
            genres = genres.replace("]", "");
            mGenres.setText(genres);
            mOverview.setText(mMovieData.getOverview());
            String thumb = mMovieData.getPoster_path();
            if (!TextUtils.isEmpty(thumb) && !"null".equals(thumb)) {
                mPoster.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mMovieData.getPoster_path()), mImageLoader);
            } else {
                mPoster.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQPqJzGtMHWmE12HmhqEYZWbmulcZVb8vhUqQcHxan7DQGNrcuF", mImageLoader);
            }
            double rating = (mMovieData.getVote_average() * 10);
            int image = RatingImage.getRatingImage(rating);
            mRatingImage.setImageResource(image);
            mRating.setText(rating + " %");
            if ((mMovieData.getTagline()) != null && !mMovieData.getTagline().isEmpty()) {
                mTagline.setVisibility(View.VISIBLE);
                mTagline.setText("Tagline: "+mMovieData.getTagline());
            }
            if (mMovieData.getBelongs_to_collection() != null) {
                if (!"null".equalsIgnoreCase(mMovieData.getBelongs_to_collection().getName()) && !TextUtils.isEmpty(mMovieData.getBelongs_to_collection().getName())) {
                    TextView textView = (TextView)findViewById(R.id.activity_selected_movie_part_of_text_view);
                    textView.setVisibility(View.VISIBLE);
                    mCollection.setVisibility(View.VISIBLE);
                    mCollection.setText(mMovieData.getBelongs_to_collection().getName());
                }
            }

            mCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MovieCollectionActivity.class);
                    intent.putExtra("collectionID", mMovieData.getBelongs_to_collection().getId());
                    startActivity(intent);
                }
            });
            ArrayList<String> languages = new ArrayList<>();
            for (int i = 0; i < mMovieData.getSpoken_languages().size(); i++) {
                languages.add(mMovieData.getSpoken_languages().get(i).getName());
            }
            String languages_spoken = String.valueOf(languages);
            languages_spoken = languages_spoken.replace("[", "");
            languages_spoken = languages_spoken.replace("]", "");
            if (!languages_spoken.isEmpty()) {
                TextView textView = (TextView)findViewById(R.id.activity_selected_movie_language_text_view);
                textView.setVisibility(View.VISIBLE);
                mLanguage.setVisibility(View.VISIBLE);
                mLanguage.setText(languages_spoken);
            }
            if (mMovieData.getHomepage() != null && !mMovieData.getHomepage().isEmpty()) {
                TextView textView = (TextView)findViewById(R.id.activity_selected_movie_homepage_text_view);
                textView.setVisibility(View.VISIBLE);
                mHomepage.setVisibility(View.VISIBLE);
                mHomepage.setText(mMovieData.getHomepage());
                mHomepage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mMovieData.getHomepage()));
                        startActivity(intent);
                    }
                });
            }
            if ((mMovieData.getBudget()) != null && !mMovieData.getBudget().isEmpty()) {
                if ((Integer.valueOf(mMovieData.getBudget())) != 0) {
                    mBudget.setVisibility(View.VISIBLE);
                    mBudget.setText("Budget: $" + mMovieData.getBudget());
                }
            }
            if ((mMovieData.getRevenue()) != null && !mMovieData.getRevenue().isEmpty()) {
                if ((Integer.valueOf(mMovieData.getRevenue())) != 0) {
                    mRevenue.setVisibility(View.VISIBLE);
                    mRevenue.setText("Revenue: $" + mMovieData.getRevenue());
                }
            }
            if ((mMovieData.getRelease_date()) != null && !mMovieData.getRelease_date().isEmpty()) {
                mReleaseDate.setVisibility(View.VISIBLE);
                mReleaseDate.setText("Release Date: "+mMovieData.getRelease_date());
            }
        }else if (response instanceof PersonCasting) {
            mPeople = (PersonCasting) response;
            if (mPeople.getCast().size() > 0) {
                TextView textView = (TextView) findViewById(R.id.activity_selected_movie_people_text_view);
                TextView textView1 = (TextView)findViewById(R.id.activity_selected_movie_people_cast_text_view);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_selected_movie_people_cast_r_v_linear_layout);
                textView.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                mPeopleCastAdapter = new PeopleRecyclerViewAdapter(mPeople, mQueue, mContext, true);
                mPeopleCastRecyclerview.setAdapter(mPeopleCastAdapter);
            }
            if (mPeople.getCrew().size() > 0) {
                TextView textView = (TextView) findViewById(R.id.activity_selected_movie_people_text_view);
                TextView textView1 = (TextView)findViewById(R.id.activity_selected_movie_people_crew_text_view);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_selected_movie_people_crew_r_v_linear_layout);
                textView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                mPeopleCrewAdapter = new PeopleRecyclerViewAdapter(mPeople, mQueue, mContext, false);
                mPeopleCrewRecyclerview.setAdapter(mPeopleCrewAdapter);
            }
        }else if (response instanceof MoviesList) {
            mRelatedMovies = (MoviesList) response;
            if (mRelatedMovies.getResults().size() > 0) {
                TextView textView = (TextView)findViewById(R.id.activity_selected_movie_related_text_view);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.activity_selected_movie_related_r_v_linear_layout);
                textView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                mRelatedMoviesAdapter = new MoviesRecyclerViewAdapter(mRelatedMovies, mQueue, mContext);
                mRelatedMoviesRecyclerView.setAdapter(mRelatedMoviesAdapter);
            }
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }
}