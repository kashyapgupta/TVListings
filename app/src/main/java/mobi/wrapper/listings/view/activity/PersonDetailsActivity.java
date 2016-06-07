package mobi.wrapper.listings.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.ImagesService;
import mobi.wrapper.listings.controller.service.PeopleService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.controller.service.ShowDetailsService;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.PersonDetails;
import mobi.wrapper.listings.model.images.Images;
import mobi.wrapper.listings.model.peopleCasting.PersonCasting;
import mobi.wrapper.listings.view.adapter.PersonCastingShowsAdapter;
import mobi.wrapper.listings.view.callback.DisplayMovie;
import mobi.wrapper.listings.view.callback.DisplayShow;

import butterknife.Bind;

/**
 * Created by Rohit on 3/18/2016.
 */
public class PersonDetailsActivity extends BaseListingActivity implements DisplayShow, ServiceCallbacks, DisplayMovie {
    String mName;
    String mHeadshot;
    PersonDetails mPersonDetails;
    int mId;
    @Bind(R.id.activity_show_person_details_headshot_networkimageview)
    NetworkImageView mHeadshotImage;

    @Bind(R.id.activity_show_person_details_no_images_text_view)
    TextView mNoImageTextView;


    @Bind(R.id.activity_show_person_details_relative_layout)
    RelativeLayout mRelativeLayout;

    @Bind(R.id.activity_show_person_details_loading_progressBar)
    ProgressBar mProgressBar;

    @Bind(R.id.activity_show_person_details_name_text_view)
    TextView mNameText;

    @Bind(R.id.activity_show_person_details_born_text_view)
    TextView mBirthdayText;

    @Bind(R.id.activity_show_person_details_biography_text_view)
    TextView mBiographyText;

    ImageLoader mImageLoader;
    RequestQueue mQueue;
    PersonCasting mPersonsCastingShows;

    @Bind(R.id.activity_show_person_details_casting_shows_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.activity_show_person_details_casting_shows_text_views)
    TextView castingShowsTextView;

    private RecyclerView.LayoutManager mLayoutManager;
    private PersonCastingShowsAdapter mPersonCastingShowAdapter;
    private Context mContext;
    Images mPersonImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Intent intent = getIntent();
        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();
        mName = intent.getStringExtra("name");
        mHeadshot = intent.getStringExtra("poster");
        mId = intent.getIntExtra("id", 1);
        Log.i("sanju", String.valueOf(mId));
        if (mHeadshot.equalsIgnoreCase("null")) {
            mHeadshot = "http://uits.knust.edu.gh/assets/images/content/pics/img2013223_17151.jpg";
        }
        mNameText.setText(mName);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ((ImagesService) TVListingServiceFactory.getInstance().getService(ImagesService.class)).getPersonImages(mId, PersonDetailsActivity.this);

        mHeadshotImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, ImagesActivity.class);
                intent1.putExtra("personId", mId);
                startActivity(intent1);
            }
        });

        ((PeopleService) TVListingServiceFactory.getInstance().getService(PeopleService.class)).getPersonDetails(mId, PersonDetailsActivity.this);

        ((ShowDetailsService) TVListingServiceFactory.getInstance().getService(ShowDetailsService.class)).personShowList(mId, PersonDetailsActivity.this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_show_person_details;
    }

    @Override
    public void displayShow(int id, double rating) {
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("rating", rating);
        startActivity(intent);
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
        if (response instanceof PersonDetails) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mPersonDetails = (PersonDetails) response;
            if (!TextUtils.isEmpty(mPersonDetails.getProfile_path()) && !"null".equalsIgnoreCase(mPersonDetails.getProfile_path())) {
                mHeadshot = String.format(UrlConstants.IMAGE_URLW_185, mPersonDetails.getProfile_path());
            }
            mHeadshotImage.setImageUrl(mHeadshot, mImageLoader);
            if (mPersonDetails.getBirthday() != null && !mPersonDetails.getBirthday().isEmpty()){
                if (mPersonDetails.getPlace_of_birth() != null && !mPersonDetails.getPlace_of_birth().isEmpty()) {
                    mBirthdayText.setText("Born on " + mPersonDetails.getBirthday()+" in " +mPersonDetails.getPlace_of_birth());
                }else {
                    mBirthdayText.setText("Born on "+ mPersonDetails.getBirthday());
                }
            }else {
                if (mPersonDetails.getPlace_of_birth() != null && !mPersonDetails.getPlace_of_birth().isEmpty()) {
                    mBirthdayText.setText("Born in "+mPersonDetails.getPlace_of_birth());
                }else {
                    mBirthdayText.setText("");
                }
            }
            mBiographyText.setText(mPersonDetails.getBiography());
        }else if (response instanceof PersonCasting) {
            mPersonsCastingShows = (PersonCasting) response;
            if (mPersonsCastingShows.getCast().size()+mPersonsCastingShows.getCrew().size() > 0) {
                castingShowsTextView.setVisibility(View.VISIBLE);
            }
            mPersonCastingShowAdapter = new PersonCastingShowsAdapter(mPersonsCastingShows, mQueue, mContext);
            mRecyclerView.setAdapter(mPersonCastingShowAdapter);
        }else if (response instanceof Images) {
            mPersonImages = (Images) response;
            if (mPersonImages.getProfiles().size() > 0) {
                mHeadshotImage.isClickable();
                if (mHeadshotImage.getBackground() == null) {
                    mHeadshotImage.setImageUrl(String.format(UrlConstants.IMAGE_URLW_500, mPersonImages.getProfiles().get(0).getFile_path()), mImageLoader);
                }
            }else {
                mHeadshotImage.setClickable(false);
                mNoImageTextView.setText("No Images To show");
            }
        }
    }
}