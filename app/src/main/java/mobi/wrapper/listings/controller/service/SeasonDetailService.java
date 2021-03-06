package mobi.wrapper.listings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.network.response.JSONGetCallback;
import mobi.wrapper.listings.controller.network.response.ResponseError;
import mobi.wrapper.listings.model.episodes.SeasonDetails;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 3/30/2016.
 */
public class SeasonDetailService extends TVListingBaseService {
    public void getSeasonDetail(int ShowId, int SeasonNo, final ServiceCallbacks callbacks) {

        String url = String.format(UrlConstants.SEASON_URL, ShowId, SeasonNo);

        TVListingNetworkClient.getInstance().get(url, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<SeasonDetails>() {
                }.getType();
                SeasonDetails seasonDetails = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(seasonDetails);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
}