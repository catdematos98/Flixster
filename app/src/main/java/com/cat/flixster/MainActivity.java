package com.cat.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.cat.flixster.models.Config;
import com.cat.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    public final static String API_KEY_PARAM = "api_key";

    //tag for logging activity
    public final static String TAG = "MovieListActivity";


    //instance fields
    AsyncHttpClient client;
    String imageBaseURL;
    String posterSize;

    //list of movies playing
    ArrayList<Movie> movies;

    //recylcer view
    RecyclerView rvMovies;
    //adapter wired to recycler
    MovieAdapter adapter;
    //image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init client
        client = new AsyncHttpClient();
        movies = new ArrayList<>();

        //init adapter -- movies array cannot be reinitiated after this point
        adapter = new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager and adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get config on app
        getConfig();


    }

    //get list of movies from API
    private void getNowPlaying(){
        //create url
        String url = API_BASE_URL + "/movie/now_playing";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.API_KEY)); //api key required
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load list of movies into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    for(int i = 0; i<results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get movies now playing", throwable, true);
                //super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    //get the configuration from the API
    private void getConfig(){
        //create url
        String url = API_BASE_URL + "/configuration";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.API_KEY)); //api key required

        //execute a get request, expect a JSON object
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config =new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", config.getImageBaseURL(), config.getPosterSize()));
                    //pass config to adapter
                    adapter.setConfig(config);
                    //gets movie list
                    getNowPlaying();
                }
                catch(JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed getting configuration", throwable, true);
            }


        });

    }

    //handle errors, log and alert users
    private void logError(String message, Throwable error, boolean alertUser){
        Log.e(TAG, message, error);

        //alert user
        if(alertUser){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
