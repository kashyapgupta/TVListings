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
import mobi.wrapper.listings.model.moviesList.MoviesList;
import mobi.wrapper.listings.view.callback.DisplayMovie;

/**
 * Created by Rohit on 4/8/2016.
 */
public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesListHolder> {
    RequestQueue mQueue1;
    MoviesList mMoviesList;
    DisplayMovie mMovie;

    public MoviesRecyclerViewAdapter(MoviesList mMoviesList, RequestQueue queue, Context context) {
        Log.i("sanju", "in mMoviesList's recycler view");
        this.mMoviesList = mMoviesList;
        mQueue1 = queue;
        mMovie = (DisplayMovie) context;
    }

    public class MoviesListHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView title;
        TextView year;
        TextView originalTitle;
        TextView rating;
        ImageView ratingImage;

        public MoviesListHolder(View itemView, int viewType) {
            super(itemView);
            ratingImage = (ImageView) itemView.findViewById(R.id.adapter_movies_recycler_view_heart_image_view);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_movies_recycler_view_poster_networkimageview);
            year = (TextView) itemView.findViewById(R.id.adapter_movies_recycler_view_year_text_view);
            title = (TextView) itemView.findViewById(R.id.adapter_movies_recycler_view_title_text_view);
            originalTitle = (TextView) itemView.findViewById(R.id.adapter_movies_recycler_view_original_title_text_view);
            rating = (TextView) itemView.findViewById(R.id.adapter_movies_recycler_view_rating_text_view);
        }
    }

    @Override
    public MoviesListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MoviesListHolder myMoviesListHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies_recycler_view, parent, false);
        myMoviesListHolder = new MoviesListHolder(view, viewType);
        return myMoviesListHolder;
    }

    @Override
    public void onBindViewHolder(MoviesListHolder holder, final int position) {
        Log.i("sanju", "in movies's holder");
        String backdrop = String.format(UrlConstants.IMAGE_URLW_500, mMoviesList.getResults().get(position).getBackdrop_path());
        if ((mMoviesList.getResults().get(position).getBackdrop_path()) != null && !mMoviesList.getResults().get(position).getBackdrop_path().isEmpty()) {
            holder.image.setImageUrl(backdrop, TVListingNetworkClient.getInstance().getImageLoader());
        }else if ((mMoviesList.getResults().get(position).getPoster_path()) != null && !mMoviesList.getResults().get(position).getPoster_path().isEmpty()){
            String poster = String.format(UrlConstants.IMAGE_URLW_500, mMoviesList.getResults().get(position).getPoster_path());
            holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
        }else {
            holder.image.setImageUrl("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSHQUB909pXldSI4TizR1eF-_j3ce2v72cavRBWpJZkZdAyqop1", TVListingNetworkClient.getInstance().getImageLoader());
        }
        final double rating = (mMoviesList.getResults().get(position).getVote_average()*10);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovie.displayMovie(mMoviesList.getResults().get(position).getId());
            }
        });
        String trimmedRating = String.format("%.1f", rating);
        holder.rating.setText(trimmedRating+" %");
        int image = RatingImage.getRatingImage(rating);
        holder.ratingImage.setImageResource(image);
        String title = mMoviesList.getResults().get(position).getTitle();
        String originalTitle = mMoviesList.getResults().get(position).getOriginal_title();
        if (title.toLowerCase().equals(originalTitle.toLowerCase())) {
            holder.title.setText(title);
        }else {
            holder.originalTitle.setText(originalTitle);
            holder.title.setText("("+title+")");
        }
        holder.year.setText(mMoviesList.getResults().get(position).getRelease_date());
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null) {
            return 0;
        }else {
            return mMoviesList.getResults().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}