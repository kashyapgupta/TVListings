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
import com.tvlistings.model.episodes.Episodes;
import com.tvlistings.view.callback.DisplayPersonDetails;

/**
 * Created by Rohit on 3/28/2016.
 */
public class EpisodeCrewRecyclerViewAdapter extends RecyclerView.Adapter<EpisodeCrewRecyclerViewAdapter.PeopleHolder> {
    RequestQueue mQueue1;
    Episodes mEpisodes;
    DisplayPersonDetails mContext;

public EpisodeCrewRecyclerViewAdapter(Episodes mEpisodes, RequestQueue queue, Context context) {
    Log.i("sanju", "in seasons recycler view");
        this.mEpisodes = mEpisodes;
        mQueue1 = queue;
        mContext = (DisplayPersonDetails) context;
        }

public class PeopleHolder extends RecyclerView.ViewHolder {
    NetworkImageView image;
    TextView characterName;
    TextView realName;

    public PeopleHolder(View itemView, int viewType) {
        super(itemView);
        image = (NetworkImageView) itemView.findViewById(R.id.adapter_episode_crew_recycler_view_poster_networkimageview);
        characterName = (TextView) itemView.findViewById(R.id.adapter_episode_crew_recycler_view_character_name_text_view);
        realName = (TextView) itemView.findViewById(R.id.adapter_episode_crew_recycler_view_real_name_text_view);
    }
}

    @Override
    public PeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PeopleHolder myPeopleHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episode_crew_recycler_view, parent, false);
        myPeopleHolder = new PeopleHolder(view, viewType);
        return myPeopleHolder;
    }

    @Override
    public void onBindViewHolder(PeopleHolder holder, final int position) {
        Log.i("sanju", "in people,s holder");
        final String poster;
        if ((mEpisodes.getCrew().get(position).getProfile_path()) != null && !mEpisodes.getCrew().get(position).getProfile_path().isEmpty()) {
            poster = String.format(UrlConstants.IMAGE_URLW_185, mEpisodes.getCrew().get(position).getProfile_path());
            holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            poster = "null";
            holder.image.setImageUrl("http://www.cens.res.in/images/noimage.png", TVListingNetworkClient.getInstance().getImageLoader());
        }holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.displayPersonDetails(mEpisodes.getCrew().get(position).getId(), mEpisodes.getCrew().get(position).getName(), poster);
            }
        });
        holder.characterName.setText(mEpisodes.getCrew().get(position).getJob());
        holder.realName.setText(mEpisodes.getCrew().get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mEpisodes == null) {
            return 0;
        }else {
            return mEpisodes.getCrew().size();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
