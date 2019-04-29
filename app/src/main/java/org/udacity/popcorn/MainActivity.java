package org.udacity.popcorn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.udacity.popcorn.moviedb.Movie;
import org.udacity.popcorn.moviedb.MovieAdapter;
import org.udacity.popcorn.moviedb.TheMovieDB;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesPreview;
    private MovieAdapter mAdapter;
    private TheMovieDB mMovieDB;
    private PosterClickListener mPosterListener;

    private class PosterClickListener implements MovieAdapter.OnPosterClickListener {
        @Override
        public void onClick(Movie movie) {
            Intent movieDetailsIntent = new Intent(MainActivity.this, MovieDetails.class);
            movieDetailsIntent.putExtra(Intent.EXTRA_TEXT, movie.original_title);
            startActivity(movieDetailsIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPreview = findViewById(R.id.thumbnails_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mMoviesPreview.setLayoutManager(layoutManager);

        mPosterListener = new PosterClickListener();
        mAdapter = new MovieAdapter(mPosterListener);
        mMoviesPreview.setAdapter(mAdapter);

        mMovieDB = new TheMovieDB(mAdapter);
        mMovieDB.fetchPopularMovies();
    }
}
