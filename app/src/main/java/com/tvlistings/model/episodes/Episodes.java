package com.tvlistings.model.episodes;

import com.tvlistings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/24/2016.
 */
public class Episodes extends BaseResponse {
    private String air_date;
    private ArrayList<Crew> crew;
    private String episode_number;
    private String season_number;
    private String name;
    private String overview;
    private int id;
    private String still_path;
    private double vote_average;
    private int vote_count;

    public Episodes(String air_date, ArrayList<Crew> crew, String episode_number, String season_number, String name, String overview, int id, String still_path, double vote_average, int vote_count) {
        this.air_date = air_date;
        this.crew = crew;
        this.episode_number = episode_number;
        this.season_number = season_number;
        this.name = name;
        this.overview = overview;
        this.id = id;
        this.still_path = still_path;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public String getAir_date() {
        return air_date;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public String getEpisode_number() {
        return episode_number;
    }

    public String getSeason_number() {
        return season_number;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    public String getStill_path() {
        return still_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

}
