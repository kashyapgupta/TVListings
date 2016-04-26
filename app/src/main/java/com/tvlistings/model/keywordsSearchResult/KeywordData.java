package com.tvlistings.model.keywordsSearchResult;

/**
 * Created by Rohit on 4/18/2016.
 */
public class KeywordData {
    private int id;
    private String name;

    public KeywordData(int id, String name) {
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
