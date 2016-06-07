package mobi.wrapper.listings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.network.response.JSONGetCallback;
import mobi.wrapper.listings.controller.network.response.ResponseError;
import mobi.wrapper.listings.model.DiscoveredData;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 4/16/2016.
 */
public class DiscoverService extends TVListingBaseService {

    public void getNetworkShows (int id, int page, final ServiceCallbacks callbacks) {
        String url = String.format(UrlConstants.NETWORK_SHOWS, id, page+1);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<DiscoveredData>() {
                }.getType();
                DiscoveredData discoveredData = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(discoveredData);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void getDiscoveredData (String url, int page, final ServiceCallbacks callbacks) {

        url = String.format(url, page+1);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<DiscoveredData>() {
                }.getType();
                DiscoveredData discoveredData = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(discoveredData);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

}
