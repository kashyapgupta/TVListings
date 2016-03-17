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
import com.tvlistings.model.trending.TrendingShows;
import com.tvlistings.view.callback.DisplayShow;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/17/2016.
 */
public class TrendingRecyclerViewAdapter extends RecyclerView.Adapter<TrendingRecyclerViewAdapter.TrendingShowHolder> {
    RequestQueue mQueue1;
    ArrayList<TrendingShows> mTrendingShows;
    DisplayShow mShow;

    public TrendingRecyclerViewAdapter(ArrayList<TrendingShows> mPeople, RequestQueue queue, Context context) {
        Log.i("sanju", "in trending recycler view");
        this.mTrendingShows = mPeople;
        mQueue1 = queue;
        mShow = (DisplayShow) context;
    }

    public class TrendingShowHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView title;
        TextView year;
        TextView rating;

        public TrendingShowHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_trending_recycler_view_poster_networkimageview);
            year = (TextView) itemView.findViewById(R.id.adapter_trending_recycler_view_year_text_view);
            title = (TextView) itemView.findViewById(R.id.adapter_trending_recycler_view_title_text_view);
            rating = (TextView) itemView.findViewById(R.id.adapter_trending_recycler_view_rating_text_view);
        }
    }

    @Override
    public TrendingShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrendingShowHolder myTrendingShowHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trending_recycler_view, parent, false);
        myTrendingShowHolder = new TrendingShowHolder(view, viewType);
        return myTrendingShowHolder;
    }

    @Override
    public void onBindViewHolder(TrendingShowHolder holder, final int position) {
        Log.i("sanju", "in trending holder");
        if ((mTrendingShows.get(position).getShow().getImages().getFanart().getThumb()) != null && !mTrendingShows.get(position).getShow().getImages().getFanart().getThumb().isEmpty()) {
            holder.image.setImageUrl(mTrendingShows.get(position).getShow().getImages().getFanart().getThumb(), TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShow.displayShow(mTrendingShows.get(position).getShow().getIds().getSlug());
            }
        });
        int rating = (int)(mTrendingShows.get(position).getShow().getRating()*10);
        holder.rating.setText(rating+" %");
        holder.title.setText(mTrendingShows.get(position).getShow().getTitle());
        holder.year.setText(mTrendingShows.get(position).getShow().getYear());
    }

    @Override
    public int getItemCount() {
        if (mTrendingShows == null) {
            return 0;
        }else {
            return mTrendingShows.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}