package com.tvlistings.model.images;

/**
 * Created by Rohit on 4/27/2016.
 */
public class ImageData {
    private String file_path;
    private String height;
    private String width;
    private double vote_average;
    private int vote_count;

    public ImageData(String file_path, String height, String width, double vote_average, int vote_count) {
        this.file_path = file_path;
        this.height = height;
        this.width = width;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public String getFile_path() {
        return file_path;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }
}
