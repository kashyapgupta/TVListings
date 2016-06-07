package mobi.wrapper.listings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.RatingImage;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.model.episodes.SeasonDetails;
import mobi.wrapper.listings.view.callback.EpisodeDetails;

/**
 * Created by Rohit on 3/16/2016.
 */
public class EpisodesRecyclerViewAdapter extends RecyclerView.Adapter<EpisodesRecyclerViewAdapter.EpisodeHolder> {
    RequestQueue mQueue1;
    SeasonDetails mEpisodes;
    int id;
    int mSeasonNo;
    EpisodeDetails mEpisodeDetails;

    public EpisodesRecyclerViewAdapter(SeasonDetails mPeople, RequestQueue queue, int id, int seasonNo, Context context) {
        Log.i("sanju", "in episodes recycler view");
        this.mEpisodes = mPeople;
        mQueue1 = queue;
        this.mSeasonNo = seasonNo;
        this.id = id;
        mEpisodeDetails = (EpisodeDetails) context;
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView number;
        TextView votes;
        TextView title;
        TextView rating;
        ImageView ratingImage;

        public EpisodeHolder(View itemView, int viewType) {
            super(itemView);
            ratingImage = (ImageView)itemView.findViewById(R.id.adapter_episode_recycler_view_heart_image_view);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_episode_recycler_view_poster_networkimageview);
            number = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_number_text_view);
            votes = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_votes_text_view);
            title = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_title_text_view);
            rating = (TextView) itemView.findViewById(R.id.adapter_episode_recycler_view_rating_text_view);
        }
    }

    @Override
    public EpisodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EpisodeHolder myEpisodeHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episode_recycler_view, parent, false);
        myEpisodeHolder = new EpisodeHolder(view, viewType);
        return myEpisodeHolder;
    }

    @Override
    public void onBindViewHolder(EpisodeHolder holder, final int position) {
        Log.i("sanju", "in epsodes holder");
        String poster = String.format(UrlConstants.IMAGE_URLW_185, mEpisodes.getEpisodes().get(position).getStill_path());
        if ((mEpisodes.getEpisodes().get(position).getStill_path()) != null && !mEpisodes.getEpisodes().get(position).getStill_path().isEmpty()) {
            holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("http://www.cens.res.in/images/noimage.png", TVListingNetworkClient.getInstance().getImageLoader());
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEpisodeDetails.episodeDetails(id, mSeasonNo, Integer.valueOf(mEpisodes.getEpisodes().get(position).getEpisode_number()));
            }
        });
        double rating = (mEpisodes.getEpisodes().get(position).getVote_average()*10);
        int image = RatingImage.getRatingImage(rating);
        holder.ratingImage.setImageResource(image);
        String trimmedRating = String.format("%.1f", rating);
        holder.rating.setText(trimmedRating+" %");
        holder.title.setText(mEpisodes.getEpisodes().get(position).getName());
        holder.number.setText("Episode "+mEpisodes.getEpisodes().get(position).getEpisode_number());
        holder.votes.setText(mEpisodes.getEpisodes().get(position).getVote_count() +" votes");
    }

    @Override
    public int getItemCount() {
        if (mEpisodes == null) {
            return 0;
        }else {
            return mEpisodes.getEpisodes().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}

