package mobi.wrapper.listings.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by rohitgarg on 3/9/16.
 */
public class JsonParser {


    public static <T> Object parseJson(String jsonString, Class<T> model) {
        try{
            Gson gson = new Gson();
            return gson.fromJson(jsonString, model);
        }catch(JsonSyntaxException e){
            return null;
        }
    }


}