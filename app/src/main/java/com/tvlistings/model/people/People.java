package com.tvlistings.model.people;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/16/2016.
 */
public class People {

    private ArrayList<Cast> cast;

    public People(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }
}
