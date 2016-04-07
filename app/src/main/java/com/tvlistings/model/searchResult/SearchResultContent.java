package com.tvlistings.model.searchResult;

import com.tvlistings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/9/2016.
 */
public class SearchResultContent extends BaseResponse {
    private ArrayList<Results> results;
    private int total_pages;

    public SearchResultContent(ArrayList<Results> results, int total_pages) {
        this.results = results;
        this.total_pages = total_pages;
    }

    public ArrayList<Results> getResults() {
        return results;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}
