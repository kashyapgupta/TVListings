package mobi.wrapper.listings.model.episodes;

/**
 * Created by Rohit on 3/24/2016.
 */
public class Crew {
    private int id;
    private String name;
    private String department;
    private String job;
    private String profile_path;

    public Crew(int id, String name, String department, String job, String profile_path) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.job = job;
        this.profile_path = profile_path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    public String getProfile_path() {
        return profile_path;
    }
}
