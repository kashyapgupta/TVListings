package mobi.wrapper.listings.controller.factory;

import mobi.wrapper.listings.controller.service.TVListingBaseService;

import java.util.HashMap;

/**
 * Created by Rohit on 3/29/2016.
 */
public class TVListingServiceFactory {
    static TVListingServiceFactory factory;
    HashMap<Class, TVListingBaseService> services = new HashMap<>();

    public static TVListingServiceFactory getInstance() {
        if(factory == null) {
            factory = new TVListingServiceFactory();
        }
        return factory;
    }
    public void addService(Class key, TVListingBaseService service) {
        services.put(key, service);
    }

    public TVListingBaseService getService(Class key) {
        return services.get(key);
    }
}