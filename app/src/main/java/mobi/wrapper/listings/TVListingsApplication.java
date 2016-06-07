package mobi.wrapper.listings;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.DiscoverService;
import mobi.wrapper.listings.controller.service.EpisodeDetailsService;
import mobi.wrapper.listings.controller.service.GenresService;
import mobi.wrapper.listings.controller.service.ImagesService;
import mobi.wrapper.listings.controller.service.MoviesDetailsService;
import mobi.wrapper.listings.controller.service.PeopleService;
import mobi.wrapper.listings.controller.service.SearchService;
import mobi.wrapper.listings.controller.service.SeasonDetailService;
import mobi.wrapper.listings.controller.service.ShowDetailsService;
import mobi.wrapper.listings.controller.service.VideosService;
import mobi.wrapper.listings.controller.util.RandomString;
import io.fabric.sdk.android.Fabric;

/**
 * Created by rohitgarg on 3/9/16.
 */
public class TVListingsApplication extends Application {
    public static final String TAG = TVListingsApplication.class.getSimpleName();
    public static RandomString randomString = new RandomString(6);
    public static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        TVListingNetworkClient.init(this);
        gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();


        TVListingServiceFactory.getInstance().addService(SearchService.class, new SearchService());
        TVListingServiceFactory.getInstance().addService(ShowDetailsService.class, new ShowDetailsService());
        TVListingServiceFactory.getInstance().addService(PeopleService.class, new PeopleService());
        TVListingServiceFactory.getInstance().addService(SeasonDetailService.class, new SeasonDetailService());
        TVListingServiceFactory.getInstance().addService(EpisodeDetailsService.class, new EpisodeDetailsService());
        TVListingServiceFactory.getInstance().addService(MoviesDetailsService.class, new MoviesDetailsService());
        TVListingServiceFactory.getInstance().addService(DiscoverService.class, new DiscoverService());
        TVListingServiceFactory.getInstance().addService(GenresService.class, new GenresService());
        TVListingServiceFactory.getInstance().addService(ImagesService.class, new ImagesService());
        TVListingServiceFactory.getInstance().addService(VideosService.class, new VideosService());
    }
}
