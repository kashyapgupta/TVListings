package com.tvlistings.controller.network;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tvlistings.TVListingsApplication;
import com.tvlistings.controller.cache.LruBitmapCache;
import com.tvlistings.controller.network.response.JSONGetCallback;
import com.tvlistings.controller.network.response.ResponseError;
import com.tvlistings.controller.util.network.VolleyErrorParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by rohitgarg on 3/9/16.
 */
public class TVListingNetworkClient {
    public static final String TAG = TVListingNetworkClient.class.getSimpleName();
    private final Context context;
    private final AssetManager assetManager;

    private RequestQueue tvListingsGetRequestQueue;
    private ImageLoader imageLoader;
    static TVListingNetworkClient instance;
    private HashMap<String, String> requestUrlToTag = new HashMap<>();

    public TVListingNetworkClient(Context context) {
        this.context = context;
        tvListingsGetRequestQueue = Volley.newRequestQueue(context);
        assetManager = context.getAssets();
    }

    public static void init(Context context) {
        instance = new TVListingNetworkClient(context);
    }

    public static TVListingNetworkClient getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return tvListingsGetRequestQueue;
    }

    /**
     * Retrieves volley image loader
     */
    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.tvListingsGetRequestQueue, new LruBitmapCache());
        }
        return this.imageLoader;
    }

    private void cancelFromRequestQueue(Request req, String tag) {
        if (null != tvListingsGetRequestQueue) {
            if (tag == null && null != req) {
                tag = requestUrlToTag.get(req.getUrl());
                tvListingsGetRequestQueue.cancelAll(tag);
            } else if (null != tag) {
                tvListingsGetRequestQueue.cancelAll(tag);
            }
        }

    }

    private void completeRequestInQueue(String url) {
        requestUrlToTag.remove(url);
    }


    private void addToRequestQueue(Request req, String tag) {
        if (null == tag) {
            tag = requestUrlToTag.get(req.getUrl());
            if (null == tag) {
                tag = TVListingsApplication.randomString.nextString();
                requestUrlToTag.put(req.getUrl(), tag);
            }
            req.setTag(tag);
        } else {
            req.setTag(tag);
        }

        cancelFromRequestQueue(req, tag);
        Log.e(TAG, "URL: -> " + req.getUrl());

        tvListingsGetRequestQueue.add(req);
    }

    public void get(String url, final JSONGetCallback jsonGetCallback) {
        get(url, jsonGetCallback, null, null);
    }
    public void get(final String inputUrl, final JSONGetCallback jsonGetCallback, String mockFile, String tag) {

        if (null != mockFile) {

            JSONObject diskFileFileResponse = readFromDiskFile(mockFile);
            if(diskFileFileResponse != null) {
                jsonGetCallback.onSuccess(diskFileFileResponse);
            } else {

                try {
                    JSONObject mockFileResponse = readFromMockFile(mockFile);
                    //JSONObject response = mockFileResponse.getJSONObject(ResponseConstants.DATA);
                    jsonGetCallback.onSuccess(mockFileResponse);

                    writeMockFileToDisk(mockFileResponse, mockFile);
                } catch (Exception e) {
                    Log.e(TAG, "Exception", e);
                }
            }

        } else {

            final String urlToHit = inputUrl;
            final JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, urlToHit, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            completeRequestInQueue(urlToHit);
                            jsonGetCallback.onSuccess(response);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            completeRequestInQueue(urlToHit);
                            jsonGetCallback.onError(getResponseError(error));
                            Log.e(TAG, "Network error", error);
                        }
                    });
            addToRequestQueue(jsonRequest, tag);
        }

    }

    private void writeMockFileToDisk(JSONObject mockFileResponse, String mockFile) {
        if(context == null || mockFileResponse == null || TextUtils.isEmpty(mockFile)) {
            return;
        }
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(mockFile, Context.MODE_PRIVATE);
            outputStream.write(mockFileResponse.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject readFromDiskFile(String mockFile) {
        if(context == null) {
            return null;
        }
        File file = new File(context.getFilesDir(), mockFile);
        if(file.exists()) {
            //Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();

                JSONArray response = null;
                try {
                    JSONObject diskFileResponse = new JSONObject(text.toString());
                    return diskFileResponse;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
        }

        return null;
    }

    private JSONObject readFromMockFile(String mockFile) throws JSONException, IOException {
        BufferedReader br = null;
        StringBuilder json = new StringBuilder();


        br = new BufferedReader(new InputStreamReader(assetManager.open(mockFile)));
        String line = null;
        while ((line = br.readLine()) != null) {
            json.append(line);
        }
        return new JSONObject(json.toString());

    }

    private ResponseError getResponseError(VolleyError error){
        ResponseError responseError = new ResponseError();
        responseError.error = error;
        responseError.msg = VolleyErrorParser.getMessage(error);
        return responseError;
    }
}
