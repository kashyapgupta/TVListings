package mobi.wrapper.listings.model.genresList;

import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.ShowContent.ShowGenres;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/18/2016.
 */
public class ShowGenresList extends BaseResponse{
    ArrayList<ShowGenres> genres;

    public ShowGenresList(ArrayList<ShowGenres> genres) {
        this.genres = genres;
    }

    public ArrayList<ShowGenres> getGenres() {
        return genres;
    }
}
