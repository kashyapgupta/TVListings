package com.tvlistings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.RatingImage;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.searchResult.Results;
import com.tvlistings.view.callback.DisplayMovie;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rohit on 4/16/2016.
 */
public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverRecyclerViewAdapter.DiscoveredListHolder> {
    RequestQueue mQueue1;
    ArrayList<Results> mDiscoveredData = new ArrayList<>();
    DisplayMovie mMovie;
    DisplayShow mShow;
    LoadMoreData mCallback;
    boolean isMovie;
    private boolean moreData;

    public DiscoverRecyclerViewAdapter(RequestQueue queue, Context context) {
        Log.i("sanju", "in mMoviesList's recycler view");
        mQueue1 = queue;
        mMovie = (DisplayMovie) context;
        mShow = (DisplayShow) context;
        mCallback = (LoadMoreData) context;
    }

    public class DiscoveredListHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView title;
        TextView originalTitle;
        TextView rating;
        ImageView ratingImage;
        TextView overview;
        RelativeLayout relativeLayout;
        CircleImageView circleImageView;
        TextView loadMore;
        ProgressBar progressBar;

        public DiscoveredListHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.adapter_discover_recycler_view_relative_layout);
                ratingImage = (ImageView) itemView.findViewById(R.id.adapter_discover_recycler_view_rating_image_view);
                image = (NetworkImageView) itemView.findViewById(R.id.adapter_discover_recycler_view_poster_network_image_view);
                overview = (TextView) itemView.findViewById(R.id.adapter_discover_recycler_view_overview_text_view);
                title = (TextView) itemView.findViewById(R.id.adapter_discover_recycler_view_title_text_view);
                originalTitle = (TextView) itemView.findViewById(R.id.adapter_discover_recycler_view_original_title_text_view);
                rating = (TextView) itemView.findViewById(R.id.adapter_discover_recycler_view_rating_text_view);
            }else {
                loadMore = (TextView) itemView.findViewById(R.id.load_more_text_view);
                circleImageView = (CircleImageView) itemView.findViewById(R.id.load_more_image_view);
                progressBar = (ProgressBar) itemView.findViewById(R.id.load_more_progressBar);
            }
        }
    }

    @Override
    public DiscoveredListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DiscoveredListHolder myDiscoveredListHolder;
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discover_recycler_view, parent, false);
            myDiscoveredListHolder = new DiscoveredListHolder(view, viewType);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);
            myDiscoveredListHolder = new DiscoveredListHolder(view, viewType);
        }
        return myDiscoveredListHolder;
    }

    @Override
    public void onBindViewHolder(final DiscoveredListHolder holder, final int position) {
        Log.i("sanju", "in movies's holder");
        if (getItemViewType(position) == 1) {
            if ((mDiscoveredData.get(position).getPoster_path()) != null && !mDiscoveredData.get(position).getPoster_path().isEmpty()) {
                String poster = String.format(UrlConstants.IMAGE_URLW_500, mDiscoveredData.get(position).getPoster_path());
                holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
            } else {
                holder.image.setImageUrl("https://c2.staticflickr.com/6/5119/5857770033_f346c22d02.jpg", TVListingNetworkClient.getInstance().getImageLoader());
            }
            final double rating = (mDiscoveredData.get(position).getVote_average() * 10);
            String trimmedRating = String.format("%.1f", rating);
            holder.rating.setText(trimmedRating + " %");
            int image = RatingImage.getRatingImage(rating);
            holder.ratingImage.setImageResource(image);
            String title;
            if (!TextUtils.isEmpty(mDiscoveredData.get(position).getTitle()) && !"null".equalsIgnoreCase(mDiscoveredData.get(position).getTitle())) {
                title = mDiscoveredData.get(position).getTitle();
                isMovie = true;
            } else {
                title = mDiscoveredData.get(position).getName();
                isMovie = false;
            }
            String originalTitle;
            if (!TextUtils.isEmpty(mDiscoveredData.get(position).getOriginal_title()) && !"null".equalsIgnoreCase(mDiscoveredData.get(position).getOriginal_title())) {
                originalTitle = mDiscoveredData.get(position).getOriginal_title();
            } else {
                originalTitle = mDiscoveredData.get(position).getOriginal_name();
            }
            if (title.toLowerCase().equals(originalTitle.toLowerCase())) {
                holder.originalTitle.setText("");
                holder.originalTitle.setVisibility(View.GONE);
                holder.title.setText(title);
            } else {
                holder.originalTitle.setVisibility(View.VISIBLE);
                holder.originalTitle.setText(originalTitle);
                holder.title.setTextSize(16);
                holder.title.setText("(" + title + ")");
            }

            holder.overview.setText(mDiscoveredData.get(position).getOverview());

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMovie) {
                        mMovie.displayMovie(mDiscoveredData.get(position).getId());
                    } else {
                        mShow.displayShow(mDiscoveredData.get(position).getId(), rating);
                    }
                }
            });
        }else {

            holder.loadMore.setVisibility(View.VISIBLE);
            holder.circleImageView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);

            holder.loadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.loadMore.setVisibility(View.GONE);
                    holder.circleImageView.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    mCallback.loadMore();
                }
            });

            holder.circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.loadMore.setVisibility(View.GONE);
                    holder.circleImageView.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    mCallback.loadMore();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDiscoveredData == null) {
            return 0;
        }else {
            if (moreData) {
                return mDiscoveredData.size() + 1;
            }else {
                return mDiscoveredData.size();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDiscoveredData.size()) {
            return 1;
        }else {
            if (moreData) {
                return 0;
            }else {
                return 1;
            }
        }
    }

    public void setData (ArrayList<Results> mDiscoveredData, boolean moreData) {
        this.moreData = moreData;
        this.mDiscoveredData.addAll(mDiscoveredData);
        notifyDataSetChanged();
    }

    public void clearData() {
        mDiscoveredData.clear();
    }
}