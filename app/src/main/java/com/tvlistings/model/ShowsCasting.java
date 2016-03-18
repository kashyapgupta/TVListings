package com.tvlistings.model;

/**
 * Created by Rohit on 3/18/2016.
 */
public class ShowsCasting {
    private String character;
    private Show show;

    public ShowsCasting(String character, Show show) {
        this.character = character;
        this.show = show;
    }

    public String getCharacter() {
        return character;
    }

    public Show getShow() {
        return show;
    }
}
