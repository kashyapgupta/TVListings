package mobi.wrapper.listings.view.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.youtube.player.YouTubePlayer;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.RatingImage;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.ImagesService;
import mobi.wrapper.listings.controller.service.MoviesDetailsService;
import mobi.wrapper.listings.controller.service.PeopleService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.controller.service.VideosService;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.images.Images;
import mobi.wrapper.listings.model.movieContents.MovieContent;
import mobi.wrapper.listings.model.moviesList.MoviesList;
import mobi.wrapper.listings.model.peopleCasting.PersonCasting;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.model.videos.Videos;
import mobi.wrapper.listings.view.adapter.MoviesRecyclerViewAdapter;
import mobi.wrapper.listings.view.adapter.PeopleRecyclerViewAdapter;
import mobi.wrapper.listings.view.adapter.VideosRecyclerViewAdapter;
import mobi.wrapper.listings.view.callback.DisplayPersonDetails;
import mobi.wrapper.listings.view.callback.DisplayVideo;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Rohit on 4/8/2016.
 */
public class MovieActivity extends BaseSearchActivity implements ServiceCallbacks, DisplayPersonDetails, DisplayVideo {
    RequestQueue mQueue;
    ImageLoader mImageLoader;
    PersonCasting mPeople;
    MovieContent mMovieData;
    MoviesList mRelatedMovies;

    @Bind(R.id.activity_selected_movie_production_flow_layout)
    FlowLayout mProductionFlowLayout;

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

    @Bind(R.id.activity_selected_movie_main_relative_layout)
    RelativeLayout mMainRelativeLayout;

    @Bind(R.id.activity_selected_movie_votes_text_view)
    TextView mVotes;

    @Bind(R.id.activity_selected_movie_description_text_view)
    TextView mOverview;

    @Bind(R.id.activity_selected_movie_tagline_text_view)
    TextView mTagline;

    @Bind(R.id.activity_selected_movie_backdrop_network_image_view)
    NetworkImageView mBackdropImage;

    @Bind(R.id.activity_selected_movie_no_images_text_view)
    TextView mNoImageTextView;

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

    @Bind(R.id.activity_selected_movie_video_recycler_view)
    RecyclerView mVideosRecyclerView;

    @Bind(R.id.activity_selected_movie_videos_text_view)
    TextView mVideosTextView;

    @Bind(R.id.activity_selected_movie_people_crew_recycler_view)
    RecyclerView mPeopleCrewRecyclerview;

    @Bind(R.id.activity_selected_movie_related_recycler_view)
    RecyclerView mRelatedMoviesRecyclerView;

    LinearLayoutManager mPeopleCastLinearLayoutManager;
    LinearLayoutManager mPeopleCrewLinearLayoutManager;
    LinearLayoutManager mRelatedMoviesLinearLayoutManager;
    LinearLayoutManager mVideosLinearLayoutManager;

    PeopleRecyclerViewAdapter mPeopleCastAdapter;
    PeopleRecyclerViewAdapter mPeopleCrewAdapter;
    MoviesRecyclerViewAdapter mRelatedMoviesAdapter;
    VideosRecyclerViewAdapter mVideosRecyclerViewAdapter;

    StringBuilder likeMovies = new StringBuilder();
    String likedMovies;
    ArrayList<Integer> moviesPreferencesList;
    Images mImages;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    int mMovieId;
    private Context mContext;
    Videos mVideos;

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

        mVideosRecyclerView.setHasFixedSize(true);
        mVideosLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mVideosRecyclerView.setLayoutManager(mVideosLinearLayoutManager);
        mVideosRecyclerViewAdapter = new VideosRecyclerViewAdapter(mQueue, mContext);
        mVideosRecyclerView.setAdapter(mVideosRecyclerViewAdapter);

        mVideosRecyclerViewAdapter.clearData();

        mBackdropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, ImagesActivity.class);
                intent1.putExtra("movieId", mMovieId);
                startActivity(intent1);
            }
        });

        //MovieDetails
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).getMovieDetail(mMovieId, MovieActivity.this);

        //Get Cast
        ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getMovieCast(mMovieId, MovieActivity.this);

        //Images
        ((ImagesService) TVListingServiceFactory.getInstance().getService(ImagesService.class)).getMovieImages(mMovieId, MovieActivity.this);

        //Similar Movies
        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).moviesList(mMovieId, MovieActivity.this);

        //Videos
        ((VideosService) TVListingServiceFactory.getInstance().getService(VideosService.class)).getMovieVideos(mMovieId, MovieActivity.this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_selected_movie;
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
                mCollapsingToolbarLayout.setTitle(title);
            }else {
                mOriginalTitle.setVisibility(View.VISIBLE);
                mOriginalTitle.setText(originalTitle);
                mCollapsingToolbarLayout.setTitle(title);
            }

            if (!TextUtils.isEmpty(mMovieData.getBackdrop_path()) && !"null".equalsIgnoreCase(mMovieData.getBackdrop_path())) {
                mBackdropImage.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mMovieData.getBackdrop_path()), mImageLoader);
                mBackgroundAppBarImageView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mMovieData.getBackdrop_path()), mImageLoader);
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

            TextView textView2;
            if (mMovieData.getProduction_companies().size() > 0) {
                TextView textView1 = (TextView)findViewById(R.id.activity_selected_movie_production_text_view);
                textView1.setVisibility(View.VISIBLE);
                for (int i = 0; i < mMovieData.getProduction_companies().size(); i++) {
                    textView2 = new TextView(this);
                    textView2.setTextSize(16);
                    textView2.setTextColor(getResources().getColor(R.color.tomato));
                    if (i < (mMovieData.getProduction_companies().size() - 1)) {
                        textView2.setText(mMovieData.getProduction_companies().get(i).getName()+", ");
                    }else {
                        textView2.setText(mMovieData.getProduction_companies().get(i).getName());
                    }
                    mProductionFlowLayout.addView(textView2);
                    final int finalI = i;
                    textView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ProductionCompanyActivity.class);
                            intent.putExtra("name", mMovieData.getProduction_companies().get(finalI).getName());
                            intent.putExtra("id", mMovieData.getProduction_companies().get(finalI).getId());
                            startActivity(intent);
                        }
                    });
                }
            }

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
        }else if (response instanceof Images) {
            mImages = (Images) response;
            if (mImages != null) {
                if (mImages.getPosters().size() + mImages.getBackdrops().size() > 0) {
                    mBackdropImage.setVisibility(View.VISIBLE);
                    if (mBackdropImage.getBackground() == null) {
                        if (mImages.getBackdrops().size() > 0) {
                            mBackdropImage.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mImages.getBackdrops().get(0).getFile_path()), mImageLoader);
                        } else if (mImages.getPosters().size() > 0) {
                            mBackdropImage.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mImages.getPosters().get(0).getFile_path()), mImageLoader);
                        }
                    }
                } else {
                    mBackdropImage.setVisibility(View.GONE);
                    mNoImageTextView.setText("No Images Available");
                }
            }
        }else if (response instanceof Videos) {
            mVideos = (Videos) response;
            if (mVideos.getResults().size() > 0) {
                mVideosTextView.setVisibility(View.VISIBLE);
                mVideosRecyclerView.setVisibility(View.VISIBLE);
                mVideosRecyclerViewAdapter.setData(mVideos);
            }else {
                mVideosTextView.setVisibility(View.GONE);
                mVideosRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showVideo(String key) {
        Intent intent = new Intent(mContext, YouTubePlayerActivity.class);
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, key);
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}