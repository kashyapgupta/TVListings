package com.tvlistings.model;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/18/2016.
 */
public class PersonsCasting {
    private ArrayList<ShowsCasting> cast;

    public PersonsCasting(ArrayList<ShowsCasting> cast) {
        this.cast = cast;
    }

    public ArrayList<ShowsCasting> getCast() {
        return cast;
    }
}
