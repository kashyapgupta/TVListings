package com.tvlistings.controller.service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvlistings.constants.UrlConstants;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.model.keywordsSearchResult.KeywordSearchResult;
import com.tvlistings.model.searchResult.SearchResultContent;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Rohit on 3/29/2016.
 */
public class SearchService extends TVListingBaseService {
    public void search(String mSearch, int mCurrentPage, final ServiceCallbacks callbacks) {

        String mUrl = String.format(UrlConstants.SEARCH_URL, mSearch, mCurrentPage + 1);

        TVListingNetworkClient.getInstance().get(mUrl, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<SearchResultContent>() {
                }.getType();
                SearchResultContent searchResultContent = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(searchResultContent);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }

    public void searchKeyword(String mSearch, int mCurrentPage, final ServiceCallbacks callbacks) {

        String mUrl = String.format(UrlConstants.SEARCH_KEYWORD, mSearch, mCurrentPage+1);

        TVListingNetworkClient.getInstance().get(mUrl, new JSONGetCallback() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                String reader = responseObject.toString();
                Type listType = new TypeToken<KeywordSearchResult>() {
                }.getType();
                KeywordSearchResult keywordSearchResult = new GsonBuilder().create().fromJson(reader, listType);
                callbacks.onSuccess(keywordSearchResult);
            }

            @Override
            public void onError(ResponseError error) {

            }
        });
    }
}
