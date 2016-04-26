package com.tvlistings.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.model.keywordsSearchResult.KeywordData;
import com.tvlistings.model.keywordsSearchResult.KeywordSearchResult;
import com.tvlistings.view.callback.LoadMoreData;
import com.tvlistings.view.callback.SelectedKeyword;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/18/2016.
 */
public class KeywordsRecyclerViewAdapter extends RecyclerView.Adapter<KeywordsRecyclerViewAdapter.KeywordHolder> {
    private final LoadMoreData mCallbacks;
    ArrayList<KeywordData> mObjects = new ArrayList<>();
    RequestQueue mQueue1;
    private boolean mMoreData;
    SelectedKeyword mKeyword;

    public KeywordsRecyclerViewAdapter(RequestQueue queue1, LoadMoreData callbacks) {
        mQueue1 = queue1;
        mCallbacks = callbacks;
        mKeyword = (SelectedKeyword) callbacks;
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

    public class KeywordHolder extends RecyclerView.ViewHolder {
        TextView name;
        ProgressBar progressBar;

        public KeywordHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                name = (TextView) itemView.findViewById(R.id.adapter_keyword_recycler_view_name_text_view);
            }else if(viewType == 0) {
                progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progressBar);
            }
        }
    }

    @Override
    public KeywordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        KeywordHolder myKeywordHolder = null;
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_keyword_recycler_view, parent, false);
            myKeywordHolder = new KeywordHolder(view, viewType);
        } else if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
            myKeywordHolder = new KeywordHolder(view, viewType);
        }
        return myKeywordHolder;
    }

    @Override
    public void onBindViewHolder(KeywordHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            holder.name.setText(mObjects.get(position).getName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mKeyword.selectedKeyword(mObjects.get(position).getId(),mObjects.get(position).getName());
                }
            });
        } else {
            mCallbacks.loadMore();
        }
    }
    public void setData(KeywordSearchResult data, boolean moreData) {
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