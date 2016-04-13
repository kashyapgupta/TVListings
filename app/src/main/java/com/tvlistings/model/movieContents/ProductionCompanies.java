package com.tvlistings.model.movieContents;

/**
 * Created by Rohit on 4/12/2016.
 */
public class ProductionCompanies {
    private String name;
    private int id;

    public ProductionCompanies(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
