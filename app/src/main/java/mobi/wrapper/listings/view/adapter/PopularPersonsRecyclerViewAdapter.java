package mobi.wrapper.listings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.model.PopularPeople;
import mobi.wrapper.listings.model.searchResult.Results;
import mobi.wrapper.listings.view.callback.DisplayMovie;
import mobi.wrapper.listings.view.callback.DisplayPersonDetails;
import mobi.wrapper.listings.view.callback.DisplayShow;
import mobi.wrapper.listings.view.callback.LoadMoreData;

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
    private boolean mMoreData = false;

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
        ProgressBar progressBar;

        public PopularPeopleHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                image = (NetworkImageView) itemView.findViewById(R.id.adapter_popular_people_recycler_view_poster_networkimageview);
                flowLayout = (FlowLayout) itemView.findViewById(R.id.adapter_popular_people_recycler_view_known_for_flow_layout);
                title = (TextView) itemView.findViewById(R.id.adapter_popular_people_recycler_view_title_text_view);
            }else if (viewType == 0) {
                progressBar = (ProgressBar)itemView.findViewById(R.id.loading_progressBar);
            }
        }
    }

    @Override
    public PopularPeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PopularPeopleHolder myPopularPeopleHolder = null;
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_popular_people_recycler_view, parent, false);
            myPopularPeopleHolder = new PopularPeopleHolder(view, viewType);
        }else if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
            myPopularPeopleHolder = new PopularPeopleHolder(view, viewType);
        }
        return myPopularPeopleHolder;
    }

    @Override
    public void onBindViewHolder(PopularPeopleHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            holder.flowLayout.removeAllViews();
            final String poster;
            Log.i("sanju", "in people,s holder");
            if ((mPopularPeople.get(position).getProfile_path()) != null && !mPopularPeople.get(position).getProfile_path().isEmpty()) {
                poster = String.format(UrlConstants.IMAGE_URLW_185, mPopularPeople.get(position).getProfile_path());
                holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
            } else {
                poster = "null";
                holder.image.setImageUrl("http://www.cens.res.in/images/noimage.png", TVListingNetworkClient.getInstance().getImageLoader());
            }
            holder.image.setOnClickListener(new View.OnClickListener() {
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
                    } else if ("movie".equalsIgnoreCase(mPopularPeople.get(position).getKnown_for().get(i).getMedia_type())) {
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
        }else {
            mContextLoadMoreData.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        if (mPopularPeople == null) {
            return 0;
        }
        if (mMoreData) {
            return mPopularPeople.size() + 1;
        }else {
            return mPopularPeople.size();
        }
    }

    public void setData (PopularPeople mPopularPeople, boolean mMoreData) {
        this.mMoreData = mMoreData;
        this.mPopularPeople.addAll(mPopularPeople.getResults());
        notifyDataSetChanged();
    }

    public void clearData() {
        this.mPopularPeople.clear();
    }


    @Override
    public int getItemViewType(int position) {
        if (position < mPopularPeople.size()) {
            return 1;
        }else {
            if (mMoreData) {
                return 0;
            }else {
                return 1;
            }
        }
    }
}