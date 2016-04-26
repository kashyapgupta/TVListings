package com.tvlistings.model.keywordsSearchResult;

import com.tvlistings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/18/2016.
 */
public class KeywordSearchResult extends BaseResponse {
    private int total_pages;
    private ArrayList<KeywordData> results;

    public KeywordSearchResult(int total_pages, ArrayList<KeywordData> results) {
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ArrayList<KeywordData> getResults() {
        return results;
    }
}
