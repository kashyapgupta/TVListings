package com.tvlistings.model;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/15/2016.
 */
public class PerticularShowContents {
    private String title;
    private IDs ids;
    private String overview;
    private String runtime;
    private double rating;
    private double votes;
    private ArrayList<String> genres;
    private ShowImage images;

    public PerticularShowContents(String title, IDs ids, String overview, String runtime, double rating, double votes, ArrayList<String> genres, ShowImage images) {
        this.title = title;
        this.ids = ids;
        this.overview = overview;
        this.runtime = runtime;
        this.rating = rating;
        this.votes = votes;
        this.genres = genres;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public IDs getIds() {
        return ids;
    }

    public String getOverview() {
        return overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public double getRating() {
        return rating;
    }

    public double getVotes() {
        return votes;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ShowImage getImages() {
        return images;
    }
}