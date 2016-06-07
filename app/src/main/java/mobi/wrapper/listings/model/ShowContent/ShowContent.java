package mobi.wrapper.listings.model.ShowContent;

import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.movieContents.ProductionCompanies;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/21/2016.
 */
public class ShowContent extends BaseResponse {
    private String backdrop_path;
    private ArrayList<Creators> created_by;
    private ArrayList<Integer> episode_run_time;
    private String first_air_date;
    private ArrayList<ShowGenres> genres;
    private int id;
    private String name;
    private String original_name;
    private ArrayList<Networks> networks;
    private int number_of_episodes;
    private int number_of_seasons;
    private String overview;
    private String poster_path;
    private ArrayList<Seasons> seasons;
    private String type;
    private double vote_average;
    private int vote_count;
    private String homepage;
    private boolean in_production;
    ArrayList<String> languages;
    private String last_air_date;
    ArrayList<ProductionCompanies> production_companies;
    private String status;

    public ShowContent(String backdrop_path, ArrayList<Creators> created_by, ArrayList<Integer> episode_run_time, String first_air_date, ArrayList<ShowGenres> genres, int id, String name, String original_name, ArrayList<Networks> networks, int number_of_episodes, int number_of_seasons, String overview, String poster_path, ArrayList<Seasons> seasons, String type, double vote_average, int vote_count, String homepage, boolean in_production, ArrayList<String> languages, String last_air_date, ArrayList<ProductionCompanies> production_companies, String status) {
        this.backdrop_path = backdrop_path;
        this.created_by = created_by;
        this.episode_run_time = episode_run_time;
        this.first_air_date = first_air_date;
        this.genres = genres;
        this.id = id;
        this.name = name;
        this.original_name = original_name;
        this.networks = networks;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this.overview = overview;
        this.poster_path = poster_path;
        this.seasons = seasons;
        this.type = type;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.homepage = homepage;
        this.in_production = in_production;
        this.languages = languages;
        this.last_air_date = last_air_date;
        this.production_companies = production_companies;
        this.status = status;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public ArrayList<Creators> getCreated_by() {
        return created_by;
    }

    public ArrayList<Integer> getEpisode_run_time() {
        return episode_run_time;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public ArrayList<ShowGenres> getGenres() {
        return genres;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public ArrayList<Networks> getNetworks() {
        return networks;
    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public ArrayList<Seasons> getSeasons() {
        return seasons;
    }

    public String getType() {
        return type;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getHomepage() {
        return homepage;
    }

    public boolean isIn_production() {
        return in_production;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public String getLast_air_date() {
        return last_air_date;
    }

    public ArrayList<ProductionCompanies> getProduction_companies() {
        return production_companies;
    }

    public String getStatus() {
        return status;
    }
    
}