package mobi.wrapper.listings.model.movieContents;

import mobi.wrapper.listings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/12/2016.
 */
public class MoviesCollectionContent extends BaseResponse {
    private int id;
    private String name;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private ArrayList<Parts> parts;

    public MoviesCollectionContent(int id, String name, String overview, String poster_path, String backdrop_path, ArrayList<Parts> parts) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.parts = parts;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public ArrayList<Parts> getParts() {
        return parts;
    }
}
