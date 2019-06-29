package com.cat.flixster.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Parcel
public class Movie {

    //valuies from API
    String title;
    String overview;
    String posterPath; //just path
    String backdropPath;
    Double voteAverage;
    ArrayList genresList;


    //empty constructor required for parcel
    public Movie() {
    }

    public Movie(JSONObject object) throws JSONException {
        genresList = new ArrayList();
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        JSONArray genres = object.getJSONArray("genre_ids");
        for(int i = 0; i<genres.length(); i++){
            int genre = genres.getInt(i);
            genresList.add(lookupGenre(genre));
        }
    // TODO - locally or get from network, list of JSON genres
        Log.i("Genre", title + "" + ": " + genresList.toString());


    }

    public String lookupGenre( int genreID){
        Map< Integer,String> genreCollection = new HashMap< Integer, String>();
        genreCollection.put(28, "Action");
        genreCollection.put(12, "Adventure");
        genreCollection.put(16, "Animation");
        genreCollection.put(35, "Comedy");
        genreCollection.put(80, "Crime");
        genreCollection.put(99, "Documentary");
        genreCollection.put(18, "Drama");
        genreCollection.put(10751, "Family");
        genreCollection.put(14, "Fantasy");
        genreCollection.put(36, "History");
        genreCollection.put(27, "Horror");
        genreCollection.put(10402, "Music");
        genreCollection.put(9648, "Mystery");
        genreCollection.put(10749, "Romance");
        genreCollection.put(878, "Science Fiction");
        genreCollection.put(10770, "TV Movie");
        genreCollection.put(53, "Thriller");
        genreCollection.put(10752, "War");
        genreCollection.put(37, "Western");

        String genre = genreCollection.get(genreID) != null ? genreCollection.get(genreID) : Integer.toString(genreID);
        return genre;
    }

    public ArrayList getGenresList() {
        return genresList;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
