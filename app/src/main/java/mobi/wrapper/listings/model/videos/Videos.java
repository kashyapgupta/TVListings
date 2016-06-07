package mobi.wrapper.listings.model.videos;

import mobi.wrapper.listings.model.BaseResponse;

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
