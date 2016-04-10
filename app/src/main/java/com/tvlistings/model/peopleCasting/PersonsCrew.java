package com.tvlistings.model.peopleCasting;

/**
 * Created by Rohit on 3/26/2016.
 */
public class PersonsCrew {
    private int episode_count;
    private String first_air_date;
    private int id;
    private String job;
    private String name;
    private String poster_path;
    private String media_type;
    private String title;
    private String release_date;
    private String profile_path;

    public PersonsCrew(int episode_count, String first_air_date, int id, String job, String name, String poster_path, String media_type, String title, String release_date, String profile_path) {
        this.episode_count = episode_count;
        this.first_air_date = first_air_date;
        this.id = id;
        this.job = job;
        this.name = name;
        this.poster_path = poster_path;
        this.media_type = media_type;
        this.title = title;
        this.release_date = release_date;
        this.profile_path = profile_path;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public int getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getProfile_path() {
        return profile_path;
    }

}