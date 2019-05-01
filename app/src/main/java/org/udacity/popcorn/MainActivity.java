package org.udacity.popcorn;

import android.content.Intent;
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

    private class PosterClickListener implements MovieAdapter.OnPosterClickListener {
        @Override
        public void onClick(Movie movie) {
            Intent movieDetailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            movieDetailsIntent.putExtra(MovieDetailsActivity.MOVIE_OBJECT, (new Gson()).toJson(movie));
            startActivity(movieDetailsIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView moviesPreview = findViewById(R.id.thumbnails_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        moviesPreview.setLayoutManager(layoutManager);

        PosterClickListener posterListener = new PosterClickListener();
        MovieAdapter adapter = new MovieAdapter(posterListener);
        moviesPreview.setAdapter(adapter);

        mMovieDB = new TheMovieDB(adapter);

        try {
            mMovieDB.fetchMoviesAndSortBy(TheMovieDB.SORT_BY.BY_POPULARITY);
        } catch (TheMovieDB.ApiNotFoundException e) {
            showMissingApiKeyToast();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.byPopularity && !item.isChecked()) {
            item.setChecked(true);
            try {
                mMovieDB.fetchMoviesAndSortBy(TheMovieDB.SORT_BY.BY_POPULARITY);
            } catch (TheMovieDB.ApiNotFoundException e) {
                showMissingApiKeyToast();
            }
            return true;
        } else if (id == R.id.byVotes && !item.isChecked()) {
            item.setChecked(true);
            try {
                mMovieDB.fetchMoviesAndSortBy(TheMovieDB.SORT_BY.BY_TOP_RATED);
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
}
