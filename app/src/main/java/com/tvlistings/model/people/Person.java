package com.tvlistings.model.people;

/**
 * Created by Rohit on 3/16/2016.
 */
public class Person {
    private String name;
    private PersonImage images;

    public Person(String name, PersonImage images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public PersonImage getImages() {
        return images;
    }
}
