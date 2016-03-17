package com.tvlistings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.Show;
import com.tvlistings.view.callback.DisplayShow;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/17/2016.
 */
public class PopularRecyclerViewAdapter extends RecyclerView.Adapter<PopularRecyclerViewAdapter.PopularShowHolder> {
    RequestQueue mQueue1;
    ArrayList<Show> mPopularShows;
    DisplayShow mShow;

    public PopularRecyclerViewAdapter(ArrayList<Show> mPeople, RequestQueue queue, Context context) {
        Log.i("sanju", "in popular's recycler view");
        this.mPopularShows = mPeople;
        mQueue1 = queue;
        mShow = (DisplayShow) context;
    }

    public class PopularShowHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView title;
        TextView year;
        TextView rating;

        public PopularShowHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_popular_recycler_view_poster_networkimageview);
            year = (TextView) itemView.findViewById(R.id.adapter_popular_recycler_view_year_text_view);
            title = (TextView) itemView.findViewById(R.id.adapter_popular_recycler_view_title_text_view);
            rating = (TextView) itemView.findViewById(R.id.adapter_popular_recycler_view_rating_text_view);
        }
    }

    @Override
    public PopularShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PopularShowHolder myPopularShowHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_popular_recycler_view, parent, false);
        myPopularShowHolder = new PopularShowHolder(view, viewType);
        return myPopularShowHolder;
    }

    @Override
    public void onBindViewHolder(PopularShowHolder holder, final int position) {
        Log.i("sanju", "in popular's holder");
        if ((mPopularShows.get(position).getImages().getFanart().getThumb()) != null && !mPopularShows.get(position).getImages().getFanart().getThumb().isEmpty()) {
            holder.image.setImageUrl(mPopularShows.get(position).getImages().getFanart().getThumb(), TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShow.displayShow(mPopularShows.get(position).getIds().getSlug());
            }
        });
        int rating = (int)(mPopularShows.get(position).getRating()*10);
        holder.rating.setText(rating+" %");
        holder.title.setText(mPopularShows.get(position).getTitle());
        holder.year.setText(mPopularShows.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        if (mPopularShows == null) {
            return 0;
        }else {
            return mPopularShows.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}