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
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.ShowContent.ShowContent;
import com.tvlistings.view.callback.DisplayEpisodes;

/**
 * Created by Rohit on 3/10/2016.
 */
public class SeasonsRecyclerViewAdapter extends RecyclerView.Adapter<SeasonsRecyclerViewAdapter.SeasonHolder> {
    ShowContent mShowData;
    RequestQueue mQueue1;
    DisplayEpisodes mContext;
    int id;

    public SeasonsRecyclerViewAdapter(ShowContent mShowData, RequestQueue queue, Context mContext, int id) {
        Log.i("sanju", "in seasons recycler view");
        this.mShowData = mShowData;
        mQueue1 = queue;
        this.mContext =(DisplayEpisodes) mContext;
        this.id = id;
    }

    public class SeasonHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView episodes;
        NetworkImageView image;

        public SeasonHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                number = (TextView) itemView.findViewById(R.id.adapter_season_recycler_view_season_number_text_view);
                episodes = (TextView) itemView.findViewById(R.id.adapter_season_recycler_view_episodes_text_view);
                image = (NetworkImageView) itemView.findViewById(R.id.adapter_season_recycler_view_poster_networkimageview);
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
            String poster = String.format(UrlConstants.IMAGE_URLW_185, mShowData.getSeasons().get(position).getPoster_path());
            if (mShowData.getSeasons().get(position).getPoster_path() != null && !mShowData.getSeasons().get(position).getPoster_path().isEmpty()) {
                holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
            } else {
                holder.image.setImageUrl("http://www.cens.res.in/images/noimage.png", TVListingNetworkClient.getInstance().getImageLoader());
            }
            holder.episodes.setText("Episodes " + mShowData.getSeasons().get(position).getEpisode_count());
            final int number = mShowData.getSeasons().get(position).getSeason_number();
            if (number == 0) {
                holder.number.setText("Season "+ "Specials");
            }else {
                holder.number.setText("Season "+ number);
            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.displayEpisodes(number, id);
                    Log.i("sanju", String.valueOf(number)+" " +String.valueOf(id));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mShowData == null) {
            return 0;
        }else {
            return mShowData.getSeasons().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
            return 1;
    }
}
