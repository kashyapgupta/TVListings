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
import com.tvlistings.model.PersonsCasting;
import com.tvlistings.view.callback.DisplayShow;

/**
 * Created by Rohit on 3/18/2016.
 */
public class PersonCastingShowsAdapter extends RecyclerView.Adapter<PersonCastingShowsAdapter.PersonShowHolder> {
    RequestQueue mQueue1;
    PersonsCasting mPersonsCasting;
    DisplayShow mShow;

    public PersonCastingShowsAdapter(PersonsCasting mPersonsCasting, RequestQueue queue, Context context) {
        Log.i("sanju", "in popular's recycler view");
        this.mPersonsCasting = mPersonsCasting;
        mQueue1 = queue;
        mShow = (DisplayShow) context;
    }

    public class PersonShowHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView title;
        TextView rating;

        public PersonShowHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_persons_casting_recycler_view_poster_networkimageview);
            title = (TextView) itemView.findViewById(R.id.adapter_persons_casting_recycler_view_title_text_view);
            rating = (TextView) itemView.findViewById(R.id.adapter_persons_casting_recycler_view_rating_text_view);
        }
    }

    @Override
    public PersonShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PersonShowHolder myPersonShowHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_persons_casting_recycler_view, parent, false);
        myPersonShowHolder = new PersonShowHolder(view, viewType);
        return myPersonShowHolder;
    }

    @Override
    public void onBindViewHolder(PersonShowHolder holder, final int position) {
        Log.i("sanju", "in popular's holder");
        if ((mPersonsCasting.getCast().get(position).getShow().getImages().getFanart().getThumb()) != null && !mPersonsCasting.getCast().get(position).getShow().getImages().getFanart().getThumb().isEmpty()) {
            holder.image.setImageUrl(mPersonsCasting.getCast().get(position).getShow().getImages().getFanart().getThumb(), TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShow.displayShow(mPersonsCasting.getCast().get(position).getShow().getIds().getSlug());
            }
        });
        int rating = (int)(mPersonsCasting.getCast().get(position).getShow().getRating()*10);
        holder.rating.setText(rating+" %");
        holder.title.setText("In " +mPersonsCasting.getCast().get(position).getShow().getTitle()+" as "+mPersonsCasting.getCast().get(position).getCharacter());
    }

    @Override
    public int getItemCount() {
        if (mPersonsCasting == null) {
            return 0;
        }else {
            return mPersonsCasting.getCast().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}