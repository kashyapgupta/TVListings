package com.tvlistings.model.videos;

import com.tvlistings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/30/2016.
 */
public class Videos extends BaseResponse {
    private ArrayList<VideoData> results;

    public Videos(ArrayList<VideoData> results) {
        this.results = results;
    }

    public ArrayList<VideoData> getResults() {
        return results;
    }
}
