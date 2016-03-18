package com.tvlistings.model.people;

import com.tvlistings.model.IDs;

/**
 * Created by Rohit on 3/16/2016.
 */
public class Person {
    private String name;
    private PersonImage images;
    private IDs ids;
    private String biography;
    private String birthday;
    private String birthplace;

    public Person(String name, PersonImage images, IDs ids, String biography, String birthday, String birthplace) {
        this.name = name;
        this.images = images;
        this.ids = ids;
        this.biography = biography;
        this.birthday = birthday;
        this.birthplace = birthplace;
    }

    public String getName() {
        return name;
    }

    public PersonImage getImages() {
        return images;
    }

    public IDs getIds() {
        return ids;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBirthplace() {
        return birthplace;
    }
}
