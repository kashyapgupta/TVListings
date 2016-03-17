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
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.SeasonContent;
import com.tvlistings.model.people.People;
import com.tvlistings.view.callback.DisplayEpisodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit on 3/10/2016.
 */
public class SeasonsRecyclerViewAdapter extends RecyclerView.Adapter<SeasonsRecyclerViewAdapter.SeasonHolder> {
    List<SeasonContent> mSeasons;
    RequestQueue mQueue1;
    DisplayEpisodes mContext;
    String mSlug;

    public SeasonsRecyclerViewAdapter(ArrayList<SeasonContent> mSeasons, RequestQueue queue, Context mContext, String slug) {
        Log.i("sanju", "in seasons recycler view");
        this.mSeasons = mSeasons;
        mQueue1 = queue;
        this.mContext =(DisplayEpisodes) mContext;
        this.mSlug = slug;
    }

    public class SeasonHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView episodes;
        TextView aired;
        TextView votes;
        NetworkImageView image;
        ImageView heartImage;
        TextView rating;

        public SeasonHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                heartImage = (ImageView) itemView.findViewById(R.id.adapter_season_recycler_view_heart_image_view);
                heartImage.setImageResource(R.drawable.heart);
                votes = (TextView) itemView.findViewById(R.id.adapter_season_recycler_view_votes_text_view);
                number = (TextView) itemView.findViewById(R.id.adapter_season_recycler_view_season_number_text_view);
                episodes = (TextView) itemView.findViewById(R.id.adapter_season_recycler_view_episodes_text_view);
                aired = (TextView) itemView.findViewById(R.id.adapter_season_recycler_view_aired_text_view);
                image = (NetworkImageView) itemView.findViewById(R.id.adapter_season_recycler_view_poster_networkimageview);
                rating = (TextView) itemView.findViewById(R.id.adapter_season_recycler_view_rating_text_view);
            }
        }
    }

    @Override
    public SeasonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SeasonHolder mySeasonHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_season_recycler_view, parent, false);
        mySeasonHolder = new SeasonHolder(view, viewType);
        return mySeasonHolder;
    }

    @Override
    public void onBindViewHolder(SeasonHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            Log.i("sanju", "in season,s holder");
            if ((mSeasons.get(position).getImages().getPoster().getThumb()) != null && !mSeasons.get(position).getImages().getPoster().getThumb().isEmpty()) {
                holder.image.setImageUrl(mSeasons.get(position).getImages().getPoster().getThumb(), TVListingNetworkClient.getInstance().getImageLoader());
            } else {
                holder.image.setImageUrl("https://cdn3.iconfinder.com/data/icons/abstract-1/512/no_image-512.png", TVListingNetworkClient.getInstance().getImageLoader());
            }
            holder.aired.setText("Aired "+mSeasons.get(position).getAired_episodes());
            holder.episodes.setText("Episodes " + mSeasons.get(position).getEpisode_count());
            final String number = mSeasons.get(position).getNumber();
            if (Integer.valueOf(number) == 0) {
                holder.number.setText("Season "+ "Specials");
            }else {
                holder.number.setText("Season "+ number);
            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.displayEpisodes(Integer.valueOf(number), mSlug);
                }
            });
            holder.votes.setText(mSeasons.get(position).getVotes() + " votes");
            int rating = (int)(mSeasons.get(position).getRating()*10);
            holder.rating.setText(String.valueOf(rating) + " %");
        }
    }

    @Override
    public int getItemCount() {
        if (mSeasons == null) {
            return 0;
        }else {
            return mSeasons.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
            return 1;
    }
}
