package mobi.wrapper.listings.model.searchResult;

/**
 * Created by Rohit on 4/14/2016.
 */
public class KnownFor {
    private String poster_path;
    private int id;
    private String overview;
    private String release_date;
    private String original_title;
    private String media_type;
    private String original_language;
    private String title;
    private  String backdrop_path;
    private int vote_count;
    private double vote_average;
    private String name;
    private String first_air_date;
    private String original_name;

    public KnownFor(String poster_path, int id, String overview, String release_date, String original_title, String media_type, String original_language, String title, String backdrop_path, int vote_count, double vote_average, String name, String first_air_date, String original_name) {
        this.poster_path = poster_path;
        this.id = id;
        this.overview = overview;
        this.release_date = release_date;
        this.original_title = original_title;
        this.media_type = media_type;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.name = name;
        this.first_air_date = first_air_date;
        this.original_name = original_name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int getVote_count() {
        return vote_count;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getName() {
        return name;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public String getOriginal_name() {
        return original_name;
    }

}
