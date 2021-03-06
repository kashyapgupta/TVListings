package mobi.wrapper.listings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.model.peopleCasting.PersonCasting;
import mobi.wrapper.listings.view.callback.DisplayMovie;
import mobi.wrapper.listings.view.callback.DisplayShow;

/**
 * Created by Rohit on 3/18/2016.
 */
public class PersonCastingShowsAdapter extends RecyclerView.Adapter<PersonCastingShowsAdapter.PersonShowHolder> {
    RequestQueue mQueue1;
    PersonCasting mPersonsCasting;
    DisplayShow mShow;
    DisplayMovie mMovie;

    public PersonCastingShowsAdapter(PersonCasting mPersonsCasting, RequestQueue queue, Context context) {
        Log.i("sanju", "in popular's recycler view");
        this.mPersonsCasting = mPersonsCasting;
        mQueue1 = queue;
        mShow = (DisplayShow) context;
        mMovie = (DisplayMovie) context;
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
        if (position < mPersonsCasting.getCast().size()) {
            String poster = String.format(UrlConstants.IMAGE_URLW_300, mPersonsCasting.getCast().get(position).getPoster_path());
            if ((mPersonsCasting.getCast().get(position).getPoster_path()) != null && !mPersonsCasting.getCast().get(position).getPoster_path().isEmpty()) {
                holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
            } else {
                holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
            }
            if (mPersonsCasting.getCast().get(position).getMedia_type().equalsIgnoreCase("tv")) {
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mShow.displayShow(mPersonsCasting.getCast().get(position).getId(), 101);
                    }
                });
                int episodes = mPersonsCasting.getCast().get(position).getEpisode_count();
                holder.rating.setText("Episodes " + episodes);
                if ((mPersonsCasting.getCast().get(position).getCharacter()) != null && !mPersonsCasting.getCast().get(position).getCharacter().isEmpty()) {
                    holder.title.setText("In " + mPersonsCasting.getCast().get(position).getName() + " as " + mPersonsCasting.getCast().get(position).getCharacter());
                }else {
                    holder.title.setText("In " + mPersonsCasting.getCast().get(position).getName());
                }
            } else {
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMovie.displayMovie(mPersonsCasting.getCast().get(position).getId());
                    }
                });
                String date = mPersonsCasting.getCast().get(position).getRelease_date();
                holder.rating.setText(date);
                if ((mPersonsCasting.getCast().get(position).getCharacter()) != null && !mPersonsCasting.getCast().get(position).getCharacter().isEmpty()) {
                    holder.title.setText("In " + mPersonsCasting.getCast().get(position).getTitle() + " as " + mPersonsCasting.getCast().get(position).getCharacter());
                }else {
                    holder.title.setText("In " + mPersonsCasting.getCast().get(position).getTitle());
                }
            }
        }else {
            final int newPosition = position - mPersonsCasting.getCast().size();
            String poster = String.format(UrlConstants.IMAGE_URLW_300, mPersonsCasting.getCrew().get(newPosition).getPoster_path());
            if ((mPersonsCasting.getCrew().get(newPosition).getPoster_path()) != null && !mPersonsCasting.getCrew().get(newPosition).getPoster_path().isEmpty()) {
                holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
            } else {
                holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
            }
            if (mPersonsCasting.getCrew().get(newPosition).getMedia_type().equalsIgnoreCase("tv")) {
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mShow.displayShow(mPersonsCasting.getCrew().get(newPosition).getId(), 101);
                    }
                });
                int episodes = mPersonsCasting.getCrew().get(newPosition).getEpisode_count();
                holder.rating.setText("Episodes " + episodes);
                holder.title.setText(mPersonsCasting.getCrew().get(newPosition).getJob()+ " of " + mPersonsCasting.getCrew().get(newPosition).getName());
            } else {
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMovie.displayMovie(mPersonsCasting.getCrew().get(newPosition).getId());
                    }
                });
                String date = mPersonsCasting.getCrew().get(newPosition).getRelease_date();
                holder.rating.setText(date);
                holder.title.setText(mPersonsCasting.getCrew().get(newPosition).getJob()+ " of " + mPersonsCasting.getCrew().get(newPosition).getTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mPersonsCasting == null) {
            return 0;
        }else {
            return mPersonsCasting.getCast().size()+mPersonsCasting.getCrew().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}