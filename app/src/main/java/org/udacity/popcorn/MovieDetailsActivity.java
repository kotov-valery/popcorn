package org.udacity.popcorn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.udacity.popcorn.moviedb.Movie;
import org.udacity.popcorn.utility.ImageLoader;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mOriginalTitle;
    private ImageView mPosterImage;
    private TextView mReleaseDate;
    private TextView mAverageRate;
    private TextView mMovieOverview;

    public static final String MOVIE_OBJECT = "MovieObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mOriginalTitle = findViewById(R.id.tv_movie_original_title);
        mPosterImage = findViewById(R.id.img_poster_preview);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mAverageRate = findViewById(R.id.tv_average_vote);
        mMovieOverview = findViewById(R.id.tv_movie_overview);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MOVIE_OBJECT)) {
            String movieJSON = intent.getStringExtra(MOVIE_OBJECT);
            Movie movie = (new Gson()).fromJson(movieJSON, Movie.class);
            fillInDetails(movie);
        }
    }

    private void fillInDetails(Movie movie) {
        ImageLoader.fetchPosterIntoView(movie.poster_path, mPosterImage);

        mOriginalTitle.setText(movie.original_title);
        mMovieOverview.setText(movie.overview);

        if (movie.vote_average != null && !movie.vote_average.isEmpty()) {
            String averageVote = movie.vote_average;
            averageVote += "/10";
            mAverageRate.setText(averageVote);
        }

        String releaseYear;
        String[] splitRelease = movie.release_date.split("-");
        if (splitRelease.length > 0) {
            releaseYear = splitRelease[0];
        } else {
            releaseYear = getString(R.string.yearIsMissingError);
        }
        mReleaseDate.setText(releaseYear);
    }
}
