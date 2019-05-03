package org.udacity.popcorn.moviedb;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheMovieDB {

    private final static String TAG = TheMovieDB.class.getSimpleName();

    private final static String API_BASE_URL = "https://api.themoviedb.org";
    private final static String API_VERSION = "3";

    //TODO: add your api key here
    private final static String API_KEY_VALUE = "";

    public static final int SORT_BY_POPULARITY = 1;
    public static final int SORT_BY_TOP_RATED = 2;

    private final MovieAdapter mAdapter;

    public TheMovieDB(MovieAdapter adapter) {
        mAdapter = adapter;
    }

    public class ApiNotFoundException extends Exception {}

    public final void fetchMoviesAndSortBy(int sortBy) throws ApiNotFoundException {
        if (API_KEY_VALUE.isEmpty()) {
            throw new ApiNotFoundException();
        }

        mAdapter.setMovies(null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDBService.TheMovieDBAPI service =
                retrofit.create(TheMovieDBService.TheMovieDBAPI.class);

        Call<Movies> call = service.fetchMovies(
                API_VERSION,
                sortBy == SORT_BY_POPULARITY ?
                        TheMovieDBService.SORT_BY_POPULARITY :
                        TheMovieDBService.SORT_BY_VOTES,
                API_KEY_VALUE);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                try {
                    Movies movies = response.body();
                    mAdapter.setMovies(movies);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.e(TAG, "Failed to request movie db API: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }


}
