package com.tvlistings.model;

/**
 * Created by Rohit on 3/14/2016.
 */
public class SeasonContent {
    private String number;
    private String votes;
    private String episode_count;
    private String aired_episodes;
    private ShowImage images;
    private double rating;

    public SeasonContent(String number, String votes, String episode_count, String aired_episodes, ShowImage images, double rating) {
        this.aired_episodes = aired_episodes;
        this.number = number;
        this.votes = votes;
        this.episode_count = episode_count;
        this.images = images;
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public String getNumber() {
        return number;
    }

    public String getVotes() {
        return votes;
    }

    public String getEpisode_count() {
        return episode_count;
    }

    public String getAired_episodes() {
        return aired_episodes;
    }

    public ShowImage getImages() {
        return images;
    }
}
