package mobi.wrapper.listings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.thefinestartist.ytpa.enums.Quality;
import com.thefinestartist.ytpa.utils.YouTubeThumbnail;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.model.videos.VideoData;
import mobi.wrapper.listings.model.videos.Videos;
import mobi.wrapper.listings.view.callback.DisplayVideo;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/30/2016.
 */
public class VideosRecyclerViewAdapter extends RecyclerView.Adapter<VideosRecyclerViewAdapter.VideosHolder> {
    RequestQueue mQueue1;
    ArrayList<VideoData> mVideos = new ArrayList<>();
    DisplayVideo mClickVideo;

    public VideosRecyclerViewAdapter(RequestQueue queue, Context context) {
        Log.i("sanju", "in videos recycler view");
        mQueue1 = queue;
        mClickVideo = (DisplayVideo) context;
    }

    public class VideosHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView title;
        TextView type;

        public VideosHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_videos_recycler_view_poster_networkimageview);
            title = (TextView) itemView.findViewById(R.id.adapter_videos_recycler_view_title_text_view);
            type = (TextView) itemView.findViewById(R.id.adapter_videos_recycler_view_type_text_view);
        }
    }

    @Override
    public VideosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideosHolder myVideosHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_videos_recycler_view, parent, false);
        myVideosHolder = new VideosHolder(view, viewType);
        return myVideosHolder;
    }

    @Override
    public void onBindViewHolder(VideosHolder holder, final int position) {
        Log.i("sanju", "in popular's holder");
        if ((mVideos.get(position).getKey()) != null && !"null".equalsIgnoreCase(mVideos.get(position).getKey())) {
            String imageUrl = YouTubeThumbnail.getUrlFromVideoId(mVideos.get(position).getKey(), Quality.HIGH);
            holder.image.setImageUrl(imageUrl, TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
        }
        if (!TextUtils.isEmpty(mVideos.get(position).getName()) && !"null".equalsIgnoreCase(mVideos.get(position).getName())) {
            holder.title.setText(mVideos.get(position).getName());
        }
        if (!TextUtils.isEmpty(mVideos.get(position).getType()) && !"null".equalsIgnoreCase(mVideos.get(position).getType())) {
            holder.type.setText(mVideos.get(position).getType());
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickVideo.showVideo(mVideos.get(position).getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mVideos == null) {
            return 0;
        }else {
            return mVideos.size();
        }
    }

    public void setData (Videos videos) {
        this.mVideos.addAll(videos.getResults());
        notifyDataSetChanged();
    }

    public void clearData () {
        mVideos.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}