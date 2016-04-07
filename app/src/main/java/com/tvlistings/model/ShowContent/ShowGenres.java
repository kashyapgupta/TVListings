package com.tvlistings.model.ShowContent;

/**
 * Created by Rohit on 3/21/2016.
 */
public class ShowGenres {
    private int id;
    private String name;

    public ShowGenres(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
