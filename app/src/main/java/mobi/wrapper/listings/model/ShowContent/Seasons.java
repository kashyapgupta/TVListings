package mobi.wrapper.listings.model.ShowContent;

/**
 * Created by Rohit on 3/21/2016.
 */
public class Seasons {
    private String air_date;
    private int episode_count;
    private int id;
    private String poster_path;
    private int season_number;

    public Seasons(String air_date, int episode_count, int id, String poster_path, int season_number) {
        this.air_date = air_date;
        this.episode_count = episode_count;
        this.id = id;
        this.poster_path = poster_path;
        this.season_number = season_number;
    }

    public String getAir_date() {
        return air_date;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getSeason_number() {
        return season_number;
    }
}
