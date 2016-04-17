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
import com.tvlistings.model.PopularPeople;
import com.tvlistings.model.searchResult.Results;
import com.tvlistings.view.callback.DisplayMovie;
import com.tvlistings.view.callback.DisplayPersonDetails;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/14/2016.
 */
public class PopularPersonsRecyclerViewAdapter extends RecyclerView.Adapter<PopularPersonsRecyclerViewAdapter.PopularPeopleHolder> {
    RequestQueue mQueue1;
    ArrayList<Results> mPopularPeople = new ArrayList<>();
    DisplayPersonDetails mPerson;
    Context mContext;
    LoadMoreData mContextLoadMoreData;
    DisplayMovie mDisplayMovie;
    DisplayShow mDisplayShow;
    private int mColor;

    public PopularPersonsRecyclerViewAdapter (RequestQueue queue, Context context, int mColor) {
        Log.i("sanju", "in seasons recycler view");
        mQueue1 = queue;
        mContext = context;
        mContextLoadMoreData = (LoadMoreData) context;
        this.mColor = mColor;
        mPerson = (DisplayPersonDetails) context;
        mDisplayMovie = (DisplayMovie) context;
        mDisplayShow = (DisplayShow) context;
    }

    public class PopularPeopleHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        FlowLayout flowLayout;
        TextView title;

        public PopularPeopleHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_popular_people_recycler_view_poster_networkimageview);
            flowLayout = (FlowLayout)itemView.findViewById(R.id.adapter_popular_people_recycler_view_known_for_flow_layout);
            title = (TextView)itemView.findViewById(R.id.adapter_popular_people_recycler_view_title_text_view);
        }
    }

    @Override
    public PopularPeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PopularPeopleHolder myPopularPeopleHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_popular_people_recycler_view, parent, false);
        myPopularPeopleHolder = new PopularPeopleHolder(view, viewType);
        return myPopularPeopleHolder;
    }

    @Override
    public void onBindViewHolder(PopularPeopleHolder holder, final int position) {
        holder.flowLayout.removeAllViews();
        final String poster;
        Log.i("sanju", "in people,s holder");
        if ((mPopularPeople.get(position).getProfile_path()) != null && !mPopularPeople.get(position).getProfile_path().isEmpty()) {
            poster = String.format(UrlConstants.IMAGE_URLW_185, mPopularPeople.get(position).getProfile_path());
            holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            poster = "null";
            holder.image.setImageUrl("http://www.cens.res.in/images/noimage.png", TVListingNetworkClient.getInstance().getImageLoader());
        }holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPerson.displayPersonDetails(mPopularPeople.get(position).getId(), mPopularPeople.get(position).getName(), poster);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPerson.displayPersonDetails(mPopularPeople.get(position).getId(), mPopularPeople.get(position).getName(), poster);
            }
        });
        holder.title.setText(mPopularPeople.get(position).getName());
        TextView textView;
        if (mPopularPeople.get(position).getKnown_for().size() > 0) {
            for (int i = 0; i < mPopularPeople.get(position).getKnown_for().size(); i++) {
                textView = new TextView(mContext);
                textView.setTextColor(mColor);
                textView.setTextSize(14);
                if ("tv".equalsIgnoreCase(mPopularPeople.get(position).getKnown_for().get(i).getMedia_type())) {
                    if (i < (mPopularPeople.get(position).getKnown_for().size() - 1)) {
                        textView.setText(mPopularPeople.get(position).getKnown_for().get(i).getName() + ", ");
                    } else {
                        textView.setText(mPopularPeople.get(position).getKnown_for().get(i).getName());
                    }
                }else if ("movie".equalsIgnoreCase(mPopularPeople.get(position).getKnown_for().get(i).getMedia_type())) {
                    if (i < (mPopularPeople.get(position).getKnown_for().size() - 1)) {
                        textView.setText(mPopularPeople.get(position).getKnown_for().get(i).getTitle() + ", ");
                    } else {
                        textView.setText(mPopularPeople.get(position).getKnown_for().get(i).getTitle());
                    }
                }
                holder.flowLayout.addView(textView);
                final int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("tv".equalsIgnoreCase(mPopularPeople.get(position).getKnown_for().get(finalI).getMedia_type())) {
                            double rating = (mPopularPeople.get(position).getKnown_for().get(finalI).getVote_average() * 10);
                            mDisplayShow.displayShow(mPopularPeople.get(position).getKnown_for().get(finalI).getId(), rating);
                        } else if ("movie".equalsIgnoreCase(mPopularPeople.get(position).getKnown_for().get(finalI).getMedia_type())) {
                            mDisplayMovie.displayMovie(mPopularPeople.get(position).getKnown_for().get(finalI).getId());
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mPopularPeople == null) {
            return 0;
        }else {
            return mPopularPeople.size();
        }
    }

    public void setData (PopularPeople mPopularPeople) {
        this.mPopularPeople.addAll(mPopularPeople.getResults());
    }

    public void clearData() {
        this.mPopularPeople.clear();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}