package com.tvlistings.model.episodes;

/**
 * Created by Rohit on 3/17/2016.
 */
public class EpisodeContent {
    private String number;
    private String title;
    private double rating;
    private int votes;
    private EpisodeImages images;
    private String overview;

    public EpisodeContent(String number, String title, double rating, int votes, EpisodeImages images, String overview) {
        this.number = number;
        this.title = title;
        this.rating = rating;
        this.votes = votes;
        this.images = images;
        this.overview = overview;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public int getVotes() {
        return votes;
    }

    public EpisodeImages getImages() {
        return images;
    }

    public String getOverview() {
        return overview;
    }
}
