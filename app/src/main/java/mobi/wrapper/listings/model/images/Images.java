package mobi.wrapper.listings.model.images;

import mobi.wrapper.listings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/27/2016.
 */
public class Images extends BaseResponse {
    private ArrayList<ImageData> backdrops;
    private ArrayList<ImageData> posters;
    private ArrayList<ImageData> profiles;

    public Images(ArrayList<ImageData> backdrops, ArrayList<ImageData> posters, ArrayList<ImageData> profiles) {
        this.backdrops = backdrops;
        this.posters = posters;
        this.profiles = profiles;
    }

    public ArrayList<ImageData> getBackdrops() {
        return backdrops;
    }

    public ArrayList<ImageData> getPosters() {
        return posters;
    }

    public ArrayList<ImageData> getProfiles() {
        return profiles;
    }
}
