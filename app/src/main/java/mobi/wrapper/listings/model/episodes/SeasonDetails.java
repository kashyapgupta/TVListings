package mobi.wrapper.listings.model.episodes;

import mobi.wrapper.listings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/24/2016.
 */
public class SeasonDetails extends BaseResponse {
    private String _id;
    ArrayList<Episodes> episodes;
    private String name;
    private String overview;
    private String id;
    private String poster_path;
    private int season_number;

    public SeasonDetails(String _id, ArrayList<Episodes> episodes, String name, String overview, String id, String poster_path, int season_number) {
        this._id = _id;
        this.episodes = episodes;
        this.name = name;
        this.overview = overview;
        this.id = id;
        this.poster_path = poster_path;
        this.season_number = season_number;
    }

    public String get_id() {
        return _id;
    }

    public ArrayList<Episodes> getEpisodes() {
        return episodes;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getSeason_number() {
        return season_number;
    }
}
