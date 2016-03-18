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
import com.tvlistings.model.people.People;
import com.tvlistings.view.callback.DisplayPersonDetails;

/**
 * Created by Rohit on 3/16/2016.
 */
public class PeopleRecyclerViewAdapter extends RecyclerView.Adapter<PeopleRecyclerViewAdapter.PeopleHolder> {
    RequestQueue mQueue1;
    People mPeople;
    DisplayPersonDetails mContext;

    public PeopleRecyclerViewAdapter(People mPeople, RequestQueue queue, Context context) {
        Log.i("sanju", "in seasons recycler view");
        this.mPeople = mPeople;
        mQueue1 = queue;
        mContext = (DisplayPersonDetails) context;
    }

    public class PeopleHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView characterName;
        TextView realName;

        public PeopleHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_people_recycler_view_poster_networkimageview);
            characterName = (TextView) itemView.findViewById(R.id.adapter_people_recycler_view_character_name_text_view);
            realName = (TextView) itemView.findViewById(R.id.adapter_people_recycler_view_real_name_text_view);
        }
    }

    @Override
    public PeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PeopleHolder myPeopleHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_people_recycler_view, parent, false);
        myPeopleHolder = new PeopleHolder(view, viewType);
        return myPeopleHolder;
    }

    @Override
    public void onBindViewHolder(PeopleHolder holder, final int position) {
        Log.i("sanju", "in people,s holder");
        if ((mPeople.getCast().get(position).getPerson().getImages().getHeadshot().getThumb()) != null && !mPeople.getCast().get(position).getPerson().getImages().getHeadshot().getThumb().isEmpty()) {
            holder.image.setImageUrl(mPeople.getCast().get(position).getPerson().getImages().getHeadshot().getThumb(), TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://cdn3.iconfinder.com/data/icons/abstract-1/512/no_image-512.png", TVListingNetworkClient.getInstance().getImageLoader());
        }holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.displayPersonDetails(mPeople.getCast().get(position).getPerson().getName(), mPeople.getCast().get(position).getPerson().getImages().getHeadshot().getThumb(), mPeople.getCast().get(position).getPerson().getImages().getFanart().getThumb(), mPeople.getCast().get(position).getPerson().getBiography(), mPeople.getCast().get(position).getPerson().getBirthday(), mPeople.getCast().get(position).getPerson().getBirthplace(), mPeople.getCast().get(position).getPerson().getIds().getSlug());
            }
        });
        holder.characterName.setText(mPeople.getCast().get(position).getCharacter());
        holder.realName.setText(mPeople.getCast().get(position).getPerson().getName());
    }

    @Override
    public int getItemCount() {
        if (mPeople == null) {
            return 0;
        }else {
            return mPeople.getCast().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}

