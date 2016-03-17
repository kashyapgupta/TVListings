package com.tvlistings.model.people;

/**
 * Created by Rohit on 3/16/2016.
 */
public class Cast {
    private String character;
    private Person person;

    public Cast(String character, Person person) {
        this.character = character;
        this.person = person;
    }

    public String getCharacter() {
        return character;
    }

    public Person getPerson() {
        return person;
    }
}
