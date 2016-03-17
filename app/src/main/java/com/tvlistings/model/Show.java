package com.tvlistings.model;

/**
 * Created by Rohit on 3/14/2016.
 */
public class Show {
    private String title;
    private String overview;
    private String year;
    private ShowImage images;
    private IDs ids;
    private double rating;

    public Show(String title, String overview, String year, ShowImage images, IDs ids, double rating) {
        this.title = title;
        this.overview = overview;
        this.year = year;
        this.images = images;
        this.ids = ids;
        this.rating = rating;
    }

    public IDs getIds() {
        return ids;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }

    public String getYear() {
        return year;
    }

    public ShowImage getImages() {
        return images;
    }
}
