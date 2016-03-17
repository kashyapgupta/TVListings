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

    public EpisodeContent(String number, String title, double rating, int votes, EpisodeImages images) {
        this.number = number;
        this.title = title;
        this.rating = rating;
        this.votes = votes;
        this.images = images;
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
}
