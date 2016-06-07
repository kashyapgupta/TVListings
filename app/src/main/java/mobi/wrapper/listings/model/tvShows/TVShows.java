package mobi.wrapper.listings.model.tvShows;

import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.searchResult.Results;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/26/2016.
 */
public class TVShows extends BaseResponse {
    private ArrayList<Results> results;

    public TVShows(ArrayList<Results> results) {
        this.results = results;
    }

    public ArrayList<Results> getResults() {
        return results;
    }
}
