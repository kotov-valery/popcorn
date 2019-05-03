package org.udacity.popcorn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import org.udacity.popcorn.moviedb.Movie;
import org.udacity.popcorn.moviedb.MovieAdapter;
import org.udacity.popcorn.moviedb.TheMovieDB;

public class MainActivity extends AppCompatActivity {

    private TheMovieDB mMovieDB;
    private MovieAdapter mAdapter;

    private static final String SORT_MODE = "sortMode";
    private int mSortMode;

    private class PosterClickListener implements MovieAdapter.OnPosterClickListener {
        @Override
        public void onClick(Movie movie) {
            Intent movieDetailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            movieDetailsIntent.putExtra(MovieDetailsActivity.MOVIE_OBJECT, (new Gson()).toJson(movie));
            startActivity(movieDetailsIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mAdapter.saveStateTo(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView moviesPreview = findViewById(R.id.thumbnails_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        moviesPreview.setLayoutManager(layoutManager);

        PosterClickListener posterListener = new PosterClickListener();
        mAdapter = new MovieAdapter(posterListener);
        moviesPreview.setAdapter(mAdapter);

        mMovieDB = new TheMovieDB(mAdapter);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        mSortMode = preferences.getInt(SORT_MODE, TheMovieDB.SORT_BY_POPULARITY);

        if (savedInstanceState != null && mAdapter.hasSavedState(savedInstanceState)) {
            mAdapter.restoreStateFrom(savedInstanceState);
        } else {
            try {
                mMovieDB.fetchMoviesAndSortBy(mSortMode);
            } catch (TheMovieDB.ApiNotFoundException e) {
                showMissingApiKeyToast();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem activeItem =
                menu.findItem(mSortMode == TheMovieDB.SORT_BY_TOP_RATED ?
                        R.id.byVotes : R.id.byPopularity);
        activeItem.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.byPopularity && !item.isChecked()) {
            item.setChecked(true);
            saveUserPreferences(TheMovieDB.SORT_BY_POPULARITY);
            try {
                mMovieDB.fetchMoviesAndSortBy(TheMovieDB.SORT_BY_POPULARITY);
            } catch (TheMovieDB.ApiNotFoundException e) {
                showMissingApiKeyToast();
            }
            return true;
        } else if (id == R.id.byVotes && !item.isChecked()) {
            item.setChecked(true);
            saveUserPreferences(TheMovieDB.SORT_BY_TOP_RATED);
            try {
                mMovieDB.fetchMoviesAndSortBy(TheMovieDB.SORT_BY_TOP_RATED);
            } catch (TheMovieDB.ApiNotFoundException e) {
                showMissingApiKeyToast();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMissingApiKeyToast() {
        String message = getString(R.string.apiKeyIsMissingError);
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void saveUserPreferences(int sortMode) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SORT_MODE, sortMode);
        editor.commit();
    }
}
