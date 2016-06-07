package mobi.wrapper.listings.model.genresList;

import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.movieContents.MovieGenres;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/18/2016.
 */
public class MovieGenresList extends BaseResponse {
    ArrayList<MovieGenres> genres;

    public MovieGenresList(ArrayList<MovieGenres> genres) {
        this.genres = genres;
    }

    public ArrayList<MovieGenres> getGenres() {
        return genres;
    }
}
