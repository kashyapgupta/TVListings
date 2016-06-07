package mobi.wrapper.listings.model;

/**
 * Created by Rohit on 3/26/2016.
 */
public class PersonDetails extends BaseResponse {
    private String biography;
    private String birthday;
    private String id;
    private String name;
    private String place_of_birth;
    private String popularity;
    private String profile_path;

    public PersonDetails(String biography, String birthday, String id, String name, String place_of_birth, String popularity, String profile_path) {
        this.biography = biography;
        this.birthday = birthday;
        this.id = id;
        this.name = name;
        this.place_of_birth = place_of_birth;
        this.popularity = popularity;
        this.profile_path = profile_path;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }
}
