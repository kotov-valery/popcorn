package org.udacity.popcorn.favorites;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.udacity.popcorn.moviedb.Movie;

public class DetailsViewModel extends ViewModel {
    private LiveData<Movie> movie;

    public DetailsViewModel(MovieDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() { return movie; }
}
