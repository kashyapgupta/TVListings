package com.tvlistings.model.people;

/**
 * Created by Rohit on 3/26/2016.
 */
public class Cast {
    private String character;
    private int id;
    private String name;
    private String profile_path;

    public Cast(String character, int id, String name, String profile_path) {
        this.character = character;
        this.id = id;
        this.name = name;
        this.profile_path = profile_path;
    }

    public String getCharacter() {
        return character;
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