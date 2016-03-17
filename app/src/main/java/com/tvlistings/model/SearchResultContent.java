package com.tvlistings.model;

/**
 * Created by Rohit on 3/9/2016.
 */
public class SearchResultContent {
    private  Show show;
    private double score;

    public SearchResultContent(Show show, double score) {
        this.show = show;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}
