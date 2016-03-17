package com.tvlistings.model.people;

/**
 * Created by Rohit on 3/16/2016.
 */
public class PersonImage {

    private Headshot headshot;
    private Fanart fanart;

    public PersonImage(Headshot headshot, Fanart fanart) {
        this.headshot = headshot;
        this.fanart = fanart;
    }

    public Headshot getHeadshot() {
        return headshot;
    }

    public Fanart getFanart() {
        return fanart;
    }
}
