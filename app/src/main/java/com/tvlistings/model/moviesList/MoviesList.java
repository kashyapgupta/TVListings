package com.tvlistings.model.moviesList;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.searchResult.Results;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/7/2016.
 */
public class MoviesList extends BaseResponse {
    ArrayList<Results> results;

    public MoviesList(ArrayList<Results> results) {
        this.results = results;
    }

    public ArrayList<Results> getResults() {
        return results;
    }
}
