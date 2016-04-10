package com.tvlistings.model.movieContents;

import com.tvlistings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/9/2016.
 */
public class MovieContent extends BaseResponse {
    private String backdrop_path;
    private String budget;
    private ArrayList<MovieGenres> genres;
    private int id;
    private String original_title;
    private String overview;
    private String poster_path;
    private String release_date;
    private String revenue;
    private String runtime;
    private String tagline;
    private String title;
    private double vote_average;
    private int vote_count;

    public MovieContent(String backdrop_path, String budget, ArrayList<MovieGenres> genres, int id, String original_title, String overview, String poster_path, String release_date, String revenue, String runtime, String tagline, String title, double vote_average, int vote_count) {
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.genres = genres;
        this.id = id;
        this.original_title = original_title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.revenue = revenue;
        this.runtime = runtime;
        this.tagline = tagline;
        this.title = title;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getBudget() {
        return budget;
    }

    public ArrayList<MovieGenres> getGenres() {
        return genres;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getRevenue() {
        return revenue;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }
}
