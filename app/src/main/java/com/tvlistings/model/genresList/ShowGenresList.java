package com.tvlistings.model.genresList;

import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.ShowContent.ShowGenres;

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
