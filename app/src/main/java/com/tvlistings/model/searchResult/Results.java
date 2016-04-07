package com.tvlistings.model.searchResult;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/21/2016.
 */
public class Results {
    private String poster_path;
    private String backdrop_path;
    private int id;
    private double vote_average;
    private String overview;
    private ArrayList<Integer> genre_ids;
    private int vote_count;
    private String name;
    private String first_air_date;
    private String original_name;

    public Results(String poster_path, String backdrop_path, int id, double vote_average, String overview, ArrayList<Integer> genre_ids, int vote_count, String name, String first_air_date, String original_name) {
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.vote_average = vote_average;
        this.overview = overview;
        this.genre_ids = genre_ids;
        this.vote_count = vote_count;
        this.name = name;
        this.first_air_date = first_air_date;
        this.original_name = original_name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int getId() {
        return id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public int getVote_count() {
        return vote_count;
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
