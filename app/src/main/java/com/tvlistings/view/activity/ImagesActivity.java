package com.tvlistings.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.ImagesService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.images.Images;
import com.tvlistings.view.adapter.GalleryImageAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rohit on 4/27/2016.
 */
public class ImagesActivity extends Activity implements ServiceCallbacks {

    @Bind(R.id.activity_show_images_gallery)
    Gallery mImagesGallery;

    @Bind(R.id.activity_show_images_loading_progressBar)
    ProgressBar mLoadingImageProgressBar;

    @Bind(R.id.activity_show_images_background_image)
    NetworkImageView mBackgroundImage;

    GalleryImageAdapter mGalleryImageAdapter;
    Context mContext;
    int mShowId, mMovieId, mPersonId;
    Images mImages;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        ButterKnife.bind(this);

        mContext = this;
        mImageLoader = TVListingNetworkClient.getInstance().getImageLoader();
        Intent intent = getIntent();
        mShowId = intent.getIntExtra("showId", 0);
        mMovieId = intent.getIntExtra("movieId", 0);
        mPersonId = intent.getIntExtra("personId", 0);
        mImagesGallery.setSpacing(15);

        if (mShowId > 0) {
            ((ImagesService) TVListingServiceFactory.getInstance().getService(ImagesService.class)).getShowImages(mShowId, ImagesActivity.this);
        }else if (mMovieId > 0) {
            ((ImagesService) TVListingServiceFactory.getInstance().getService(ImagesService.class)).getMovieImages(mMovieId, ImagesActivity.this);
        }else if (mPersonId > 0) {
            ((ImagesService) TVListingServiceFactory.getInstance().getService(ImagesService.class)).getPersonImages(mPersonId, ImagesActivity.this);
        }

        mImagesGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mImagesGallery.setVisibility(View.GONE);
                mBackgroundImage.setVisibility(View.VISIBLE);
                mLoadingImageProgressBar.setVisibility(View.VISIBLE);
                if (mGalleryImageAdapter.mImages.getProfiles() != null) {
                    mBackgroundImage.setImageUrl(String.format(UrlConstants.IMAGE_URLW_1000, mGalleryImageAdapter.mImages.getProfiles().get(position).getFile_path()), mImageLoader);
                }else {
                    if (position < mGalleryImageAdapter.mImages.getBackdrops().size()) {
                        mBackgroundImage.setImageUrl(String.format(UrlConstants.IMAGE_URLW_1000, mGalleryImageAdapter.mImages.getBackdrops().get(position).getFile_path()), mImageLoader);
                    } else {
                        int newPosition = position - mGalleryImageAdapter.mImages.getBackdrops().size();
                        mBackgroundImage.setImageUrl(String.format(UrlConstants.IMAGE_URLW_1000, mGalleryImageAdapter.mImages.getPosters().get(newPosition).getFile_path()), mImageLoader);
                    }
                }
            }
        });
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof Images) {
            mImages = (Images) response;
            mGalleryImageAdapter = new GalleryImageAdapter(mContext, mImages);
            mImagesGallery.setAdapter(mGalleryImageAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if (mBackgroundImage.getVisibility() == View.VISIBLE) {
            mBackgroundImage.setVisibility(View.GONE);
            mLoadingImageProgressBar.setVisibility(View.GONE);
            mImagesGallery.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }
}
