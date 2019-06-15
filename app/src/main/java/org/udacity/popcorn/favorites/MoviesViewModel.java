package org.udacity.popcorn.favorites;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import org.udacity.popcorn.moviedb.Movie;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {
    private static final String TAG = MoviesViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;

    public MoviesViewModel(Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the DataBase");
        movies = database.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
