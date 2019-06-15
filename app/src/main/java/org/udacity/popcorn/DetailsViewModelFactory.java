package org.udacity.popcorn;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import org.udacity.popcorn.favorites.DetailsViewModel;
import org.udacity.popcorn.favorites.MovieDatabase;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDatabase mDb;
    private final int mMovieId;

    public DetailsViewModelFactory(MovieDatabase database, int movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel(mDb, mMovieId);
    }
}
