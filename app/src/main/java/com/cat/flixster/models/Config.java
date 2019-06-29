package com.cat.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    private String imageBaseURL;
    String posterSize;
    String backdropSize;

    public Config(JSONObject  object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get image base URL
        imageBaseURL = images.getString("secure_base_url");
        //get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //getting 4th size or use default
        posterSize = posterSizeOptions.optString(3, "w342");
        //parse the backdrop sizes and use the option at index 1 or w780
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    //helper method for creating urls
    public String getImageURL(String size, String path){
        return String.format("%s%s%s", imageBaseURL, size, path);
    }

    public String getBackdropSize() {
        return backdropSize;
    }

    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
