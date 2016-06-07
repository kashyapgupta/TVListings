package mobi.wrapper.listings.model;

import mobi.wrapper.listings.model.searchResult.Results;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/16/2016.
 */
public class DiscoveredData extends BaseResponse {
    private int total_pages;
    private ArrayList<Results> results;

    public DiscoveredData(int total_pages, ArrayList<Results> results) {
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ArrayList<Results> getResults() {
        return results;
    }
}
