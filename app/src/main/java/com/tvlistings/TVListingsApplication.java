package com.tvlistings;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.EpisodeDetailsService;
import com.tvlistings.controller.service.PeopleService;
import com.tvlistings.controller.service.SearchService;
import com.tvlistings.controller.service.SeasonDetailService;
import com.tvlistings.controller.service.ShowDetailsService;
import com.tvlistings.controller.util.RandomString;

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

        TVListingNetworkClient.init(this);
        gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();


        TVListingServiceFactory.getInstance().addService(SearchService.class, new SearchService());
        TVListingServiceFactory.getInstance().addService(ShowDetailsService.class, new ShowDetailsService());
        TVListingServiceFactory.getInstance().addService(PeopleService.class, new PeopleService());
        TVListingServiceFactory.getInstance().addService(SeasonDetailService.class, new SeasonDetailService());
        TVListingServiceFactory.getInstance().addService(EpisodeDetailsService.class, new EpisodeDetailsService());
    }
}
