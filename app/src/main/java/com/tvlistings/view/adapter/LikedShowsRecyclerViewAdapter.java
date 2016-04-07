package com.tvlistings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.RatingImage;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.ShowContent.ShowContent;
import com.tvlistings.view.callback.DisplayShow;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/5/2016.
 */
public class LikedShowsRecyclerViewAdapter extends RecyclerView.Adapter<LikedShowsRecyclerViewAdapter.LikedShowHolder> {
    RequestQueue mQueue1;
    ArrayList<ShowContent> mLikedShowData;
    DisplayShow mShow;

    public LikedShowsRecyclerViewAdapter(ArrayList<ShowContent> mLikedShowData, RequestQueue queue, Context context) {
        Log.i("sanju", "in popular's recycler view");
        this.mLikedShowData = mLikedShowData;
        mQueue1 = queue;
        mShow = (DisplayShow) context;
    }

    public class LikedShowHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView title;
        TextView year;
        TextView originalTitle;
        TextView rating;
        ImageView ratingImage;

        public LikedShowHolder(View itemView, int viewType) {
            super(itemView);
            ratingImage = (ImageView) itemView.findViewById(R.id.adapter_liked_shows_recycler_view_heart_image_view);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_liked_shows_recycler_view_poster_networkimageview);
            year = (TextView) itemView.findViewById(R.id.adapter_liked_shows_recycler_view_year_text_view);
            title = (TextView) itemView.findViewById(R.id.adapter_liked_shows_recycler_view_title_text_view);
            originalTitle = (TextView) itemView.findViewById(R.id.adapter_liked_shows_recycler_view_original_title_text_view);
            rating = (TextView) itemView.findViewById(R.id.adapter_liked_shows_recycler_view_rating_text_view);
        }
    }

    @Override
    public LikedShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LikedShowHolder myLikedShowHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_liked_shows_recycler_view, parent, false);
        myLikedShowHolder = new LikedShowHolder(view, viewType);
        return myLikedShowHolder;
    }

    @Override
    public void onBindViewHolder(LikedShowHolder holder, final int position) {
        Log.i("sanju", "in popular's holder");
        String backdrop = String.format(UrlConstants.IMAGE_URLW_500, mLikedShowData.get(position).getBackdrop_path());
        if ((mLikedShowData.get(position).getBackdrop_path()) != null && !mLikedShowData.get(position).getBackdrop_path().isEmpty()) {
            holder.image.setImageUrl(backdrop, TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
        }
        final double rating = (mLikedShowData.get(position).getVote_average()*10);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShow.displayShow(mLikedShowData.get(position).getId(), rating);
            }
        });
        String trimmedRating = String.format("%.1f", rating);
        holder.rating.setText(trimmedRating+" %");
        int image = RatingImage.getRatingImage(rating);
        holder.ratingImage.setImageResource(image);
        String name = mLikedShowData.get(position).getName();
        String originalName = mLikedShowData.get(position).getOriginal_name();
        if (name.toLowerCase().equals(originalName.toLowerCase())) {
            holder.title.setText(name);
        }else {
            holder.originalTitle.setText(originalName);
            holder.title.setText("("+name+")");
        }
        holder.year.setText(mLikedShowData.get(position).getFirst_air_date());
    }

    @Override
    public int getItemCount() {
        if (mLikedShowData == null) {
            return 0;
        }else {
            return mLikedShowData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
