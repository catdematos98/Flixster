package com.cat.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cat.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle;
    TextView tvOveriew;
    RatingBar rbUpVote;
    TextView tvGenres;
    //ImageView ivBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOveriew = (TextView) findViewById(R.id.tvOverview);
        rbUpVote = (RatingBar) findViewById(R.id.ratingBar);
        tvGenres = (TextView) findViewById(R.id.tvGenres);
        //ivBackdrop = (ImageView) findViewById(R.id.ivBackdropImage);

        tvTitle.setText(movie.getTitle());
        tvOveriew.setText(movie.getOverview());
        tvGenres.setText(movie.getGenresList().toString());


        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbUpVote.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

    }
}
