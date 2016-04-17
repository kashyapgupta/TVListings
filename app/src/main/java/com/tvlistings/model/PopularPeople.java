package com.tvlistings.model;

import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.searchResult.Results;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/14/2016.
 */
public class PopularPeople extends BaseResponse {
    ArrayList<Results> results;

    public PopularPeople(ArrayList<Results> results) {
        this.results = results;
    }

    public ArrayList<Results> getResults() {
        return results;
    }
}
