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
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.tvlistings.R;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.model.SearchResultContent;
import com.tvlistings.view.callback.DisplayShow;
import com.tvlistings.view.callback.LoadMoreData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit on 3/10/2016.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.MyObjectHolder> {
    private final LoadMoreData mCallbacks;
    private final DisplayShow mSeason;
    List<SearchResultContent> mObjects;
    RequestQueue mQueue1;
    private boolean mMoreData;

    public SearchRecyclerViewAdapter(RequestQueue queue1, LoadMoreData callbacks) {
        mQueue1 = queue1;
        mCallbacks = callbacks;
        mSeason = (DisplayShow) callbacks;
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
            if((mObjects.get(position).getShow().getImages().getPoster().getThumb()) != null && !(mObjects.get(position).getShow().getImages().getPoster().getThumb()).isEmpty()) {
                holder.image.setImageUrl(mObjects.get(position).getShow().getImages().getPoster().getThumb(), TVListingNetworkClient.getInstance().getImageLoader());
            }else {
                holder.image.setImageUrl("http://www.hellocomic.com/images/no_image_available.png", TVListingNetworkClient.getInstance().getImageLoader());
            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("sanju","in onClick");
                    Log.i("sanju", mObjects.get(position).getShow().getIds().getSlug());
                    mSeason.displayShow(mObjects.get(position).getShow().getIds().getSlug());
                }
            });
            holder.title.setText(mObjects.get(position).getShow().getTitle());
            holder.year.setText(mObjects.get(position).getShow().getYear());
        } else {
            mCallbacks.loadMore();
        }
    }
    public void setData(ArrayList<SearchResultContent> data, boolean moreData, boolean newData) {
        if(mObjects == null) {
            mObjects = new ArrayList<>();
        }
        this.mMoreData = moreData;
        if (newData) {
            mObjects.clear();
        }
        mObjects.addAll(data);
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
}
