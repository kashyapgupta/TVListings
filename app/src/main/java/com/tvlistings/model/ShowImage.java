package com.tvlistings.model;

import com.tvlistings.model.people.Fanart;

/**
 * Created by Rohit on 3/14/2016.
 */
public class ShowImage {
    private Poster poster;
    private Fanart fanart;

    public ShowImage(Poster poster, Fanart fanart) {
        this.poster = poster;
        this.fanart = fanart;
    }

    public Poster getPoster() {
        return poster;
    }

    public Fanart getFanart() {
        return fanart;
    }
}
