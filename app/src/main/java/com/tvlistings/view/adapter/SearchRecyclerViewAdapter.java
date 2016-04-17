package com.tvlistings.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.searchResult.Results;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.callback.DisplayMovie;
import com.tvlistings.view.callback.DisplayPersonDetails;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/10/2016.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.MyObjectHolder> {
    private final LoadMoreData mCallbacks;
    private final DisplayShow mSeason;
    ArrayList<Results> mObjects = new ArrayList<>();
    RequestQueue mQueue1;
    private final DisplayPersonDetails mPerson;
    private final DisplayMovie mMovie;
    private boolean mMoreData;

    public SearchRecyclerViewAdapter(RequestQueue queue1, LoadMoreData callbacks) {
        mQueue1 = queue1;
        mCallbacks = callbacks;
        mSeason = (DisplayShow) callbacks;
        mPerson = (DisplayPersonDetails) callbacks;
        mMovie = (DisplayMovie) callbacks;
        Log.i("sanju", "in Recycler view");
    }

    @Override
    public int getItemCount() {
        if(mObjects == null) {
            return 0;
        }
        if(mMoreData) {
            return mObjects.size() + 1;
        } else {
            return mObjects.size();
        }
    }

    public class MyObjectHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView year;
        NetworkImageView image;
        ProgressBar progressBar;

        public MyObjectHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                title = (TextView) itemView.findViewById(R.id.adapter_search_recycler_view_title_text_view);
                year = (TextView) itemView.findViewById(R.id.adapter_search_recycler_view_year_text_view);
                image = (NetworkImageView) itemView.findViewById(R.id.adapter_search_recycler_view_poster_networkimageview);
            }else if(viewType == 0) {
                progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progressBar);
            }
        }
    }

    @Override
    public MyObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyObjectHolder myObjectHolder = null;
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_search_recycler_view, parent, false);
            myObjectHolder = new MyObjectHolder(view, viewType);
        } else if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
            myObjectHolder = new MyObjectHolder(view, viewType);
        }
        return myObjectHolder;
    }

    @Override
    public void onBindViewHolder(MyObjectHolder holder, final int position) {
        if (getItemViewType(position) == 1) {

            if (mObjects.get(position).getMedia_type().equalsIgnoreCase("tv")) {

                if((mObjects.get(position).getPoster_path()) != null && !(mObjects.get(position).getPoster_path().isEmpty())) {
                    String imageURL = String.format(UrlConstants.IMAGE_URLW_185,mObjects.get(position).getPoster_path());
                    holder.image.setImageUrl(imageURL, TVListingNetworkClient.getInstance().getImageLoader());
                }else {
                    holder.image.setImageUrl("http://www.hellocomic.com/images/no_image_available.png", TVListingNetworkClient.getInstance().getImageLoader());
                }

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("sanju", "in onClick");
                        Log.i("sanju", String.valueOf(mObjects.get(position).getId()));
                        double rating = (mObjects.get(position).getVote_average() * 10);
                        mSeason.displayShow(mObjects.get(position).getId(), rating);
                    }
                });

                holder.title.setText(mObjects.get(position).getName());
                holder.year.setText(mObjects.get(position).getFirst_air_date());

            }else if (mObjects.get(position).getMedia_type().equalsIgnoreCase("movie")) {

                if((mObjects.get(position).getPoster_path()) != null && !(mObjects.get(position).getPoster_path().isEmpty())) {
                    String imageURL = String.format(UrlConstants.IMAGE_URLW_185,mObjects.get(position).getPoster_path());
                    holder.image.setImageUrl(imageURL, TVListingNetworkClient.getInstance().getImageLoader());
                }else {
                    holder.image.setImageUrl("http://www.hellocomic.com/images/no_image_available.png", TVListingNetworkClient.getInstance().getImageLoader());
                }

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMovie.displayMovie(mObjects.get(position).getId());
                    }
                });

                holder.title.setText(mObjects.get(position).getTitle());
                holder.year.setText(mObjects.get(position).getRelease_date());

            }else if (mObjects.get(position).getMedia_type().equalsIgnoreCase("person")) {

                final String poster;

                if((mObjects.get(position).getProfile_path()) != null && !(mObjects.get(position).getProfile_path().isEmpty())) {
                    poster = String.format(UrlConstants.IMAGE_URLW_185,mObjects.get(position).getProfile_path());
                    holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
                }else {
                    poster = "null";
                    holder.image.setImageUrl("http://www.hellocomic.com/images/no_image_available.png", TVListingNetworkClient.getInstance().getImageLoader());
                }

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPerson.displayPersonDetails(mObjects.get(position).getId(), mObjects.get(position).getName(), poster);
                    }
                });
                holder.title.setText(mObjects.get(position).getName());
                if (mObjects.get(position).getKnown_for().size() > 0) {
                    if ("movie".equalsIgnoreCase(mObjects.get(position).getKnown_for().get(0).getMedia_type())) {
                        holder.year.setText("Known for : "+mObjects.get(position).getKnown_for().get(0).getTitle());
                    }else if ("tv".equalsIgnoreCase(mObjects.get(position).getKnown_for().get(0).getMedia_type())) {
                        holder.year.setText("Known for : "+mObjects.get(position).getKnown_for().get(0).getName());
                    }else {
                        holder.year.setText("");
                    }
                }
            }

        } else {
            mCallbacks.loadMore();
        }
    }
    public void setData(SearchResultContent data, boolean moreData) {
        this.mMoreData = moreData;
        mObjects.addAll(data.getResults());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mObjects.size()) {
            return 1;
        }else {
            if(mMoreData) {
                return 0;
            } else {
                return 1;
            }
        }
    }
    public void clearData() {
        mObjects.clear();
    }
}
