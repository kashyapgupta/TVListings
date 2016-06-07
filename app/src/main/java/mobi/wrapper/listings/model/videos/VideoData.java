package mobi.wrapper.listings.model.videos;

/**
 * Created by Rohit on 4/30/2016.
 */
public class VideoData {
    private String key;
    private String name;
    private String type;

    public VideoData(String key, String name, String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
