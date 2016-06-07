package mobi.wrapper.listings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.RatingImage;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.MoviesDetailsService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.movieContents.MoviesCollectionContent;
import mobi.wrapper.listings.model.searchResult.SearchResultContent;
import mobi.wrapper.listings.view.adapter.MoviesCollectionRecyclerViewAdapter;
import mobi.wrapper.listings.view.callback.DisplayMovie;

import butterknife.Bind;

/**
 * Created by Rohit on 4/12/2016.
 */
public class MovieCollectionActivity extends BaseSearchActivity implements ServiceCallbacks,DisplayMovie {

    int mCollectionId;
    MoviesCollectionContent mCollectionData;

    MoviesCollectionRecyclerViewAdapter mMoviesCollectionAdapter;

    @Bind(R.id.activity_movie_collection_main_relative_layout)
    RelativeLayout mMainRelativeLayout;

    @Bind(R.id.activity_movie_collection_rating_image_view)
    ImageView mRatingImage;

    @Bind(R.id.activity_movie_collection_rating_text_view)
    TextView mRating;

    @Bind(R.id.activity_movie_collection_overview_text_view)
    TextView mOverview;

    @Bind(R.id.activity_movie_collection_movies_recycler_view)
    RecyclerView mMovies;

    @Bind(R.id.activity_movie_collection_poster_background_network_image_view)
    NetworkImageView mBackground;

    private ImageLoader mImageLoader;
    LinearLayoutManager mLinearLayoutManager;
    private RequestQueue mQueue;
    Context mContext;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_movie_collection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mCollectionId = intent.getIntExtra("collectionID", 0);
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();

        mMovies.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mMovies.setLayoutManager(mLinearLayoutManager);

        ((MoviesDetailsService) TVListingServiceFactory.getInstance().getService(MoviesDetailsService.class)).getCollectionDetail(mCollectionId, MovieCollectionActivity.this);

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
        if (response instanceof MoviesCollectionContent) {
            mCollectionData = (MoviesCollectionContent) response;
            if (mCollectionData.getBackdrop_path() != null && !mCollectionData.getBackdrop_path().isEmpty()) {
                mBackgroundAppBarImageView.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mCollectionData.getBackdrop_path()), mImageLoader);
            }
            if (mCollectionData.getPoster_path() != null && !mCollectionData.getPoster_path().isEmpty()) {
                mBackground.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mCollectionData.getPoster_path()), mImageLoader);
            }
            mCollapsingToolbarLayout.setTitle(mCollectionData.getName());
            double rating = 0;
            for (int i = 0; i < mCollectionData.getParts().size(); i++) {
                rating = rating + mCollectionData.getParts().get(i).getVote_average();
            }
            rating = (rating / mCollectionData.getParts().size()) * 10;
            int image = RatingImage.getRatingImage(rating);
            String ratingTrimmed = String.format("%.1f", rating);
            mRatingImage.setImageResource(image);
            mRating.setText(ratingTrimmed + " %");
            if (mCollectionData.getOverview() != null && !mCollectionData.getOverview().isEmpty()) {
                mOverview.setText(mCollectionData.getOverview());
            }

            if (mCollectionData.getParts().size() > 0) {
                TextView textView = (TextView)findViewById(R.id.activity_movie_collection_movies_text_view);
                textView.setVisibility(View.VISIBLE);
                mMoviesCollectionAdapter = new MoviesCollectionRecyclerViewAdapter(mCollectionData.getParts(), mQueue, mContext);
                mMovies.setAdapter(mMoviesCollectionAdapter);
            }
        }else if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }
    }
}