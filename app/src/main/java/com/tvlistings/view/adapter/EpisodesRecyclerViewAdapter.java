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
import com.tvlistings.model.episodes.EpisodeContent;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/16/2016.
 */
public class EpisodesRecyclerViewAdapter extends RecyclerView.Adapter<EpisodesRecyclerViewAdapter.EpisodeHolder> {
    RequestQueue mQueue1;
    ArrayList<EpisodeContent> mEpisodes;
    String mSlug;
    int mSeasonNo;

    public EpisodesRecyclerViewAdapter(ArrayList<EpisodeContent> mPeople, RequestQueue queue, String slug, int seasonNo, Context context) {
        Log.i("sanju", "in episodes recycler view");
        this.mEpisodes = mPeople;
        mQueue1 = queue;
        this.mSeasonNo = seasonNo;
        this.mSlug = slug;
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView number;
        TextView votes;
        TextView title;
        TextView rating;

        public EpisodeHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_episode_recycler_view_poster_networkimageview);
            number = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_number_text_view);
            votes = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_votes_text_view);
            title = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_title_text_view);
            rating = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_rating_text_view);
        }
    }

    @Override
    public EpisodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EpisodeHolder myEpisodeHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episode_recycler_view, parent, false);
        myEpisodeHolder = new EpisodeHolder(view, viewType);
        return myEpisodeHolder;
    }

    @Override
    public void onBindViewHolder(EpisodeHolder holder, int position) {
        Log.i("sanju", "in epsodes holder");
        if ((mEpisodes.get(position).getImages().getScreenshot().getThumb()) != null && !mEpisodes.get(position).getImages().getScreenshot().getThumb().isEmpty()) {
            holder.image.setImageUrl(mEpisodes.get(position).getImages().getScreenshot().getThumb(), TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://cdn3.iconfinder.com/data/icons/abstract-1/512/no_image-512.png", TVListingNetworkClient.getInstance().getImageLoader());
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        int rating = (int)(mEpisodes.get(position).getRating()*10);
        holder.rating.setText(rating+" %");
        holder.title.setText(mEpisodes.get(position).getTitle());
        holder.number.setText("Episode "+mEpisodes.get(position).getNumber());
        holder.votes.setText(mEpisodes.get(position).getVotes() +" votes");
    }

    @Override
    public int getItemCount() {
        if (mEpisodes == null) {
            return 0;
        }else {
            return mEpisodes.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}

