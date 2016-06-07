package mobi.wrapper.listings.model.movieContents;

/**
 * Created by Rohit on 4/9/2016.
 */
public class MovieGenres {
    private int id;
    private String name;

    public MovieGenres(int id, String name) {
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
