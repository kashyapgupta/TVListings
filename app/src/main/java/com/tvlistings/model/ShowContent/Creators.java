package com.tvlistings.model.ShowContent;

/**
 * Created by Rohit on 3/21/2016.
 */
public class Creators {
    private int id;
    private String name;
    private String profile_path;

    public Creators(int id, String name, String profile_path) {
        this.id = id;
        this.name = name;
        this.profile_path = profile_path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }
}
