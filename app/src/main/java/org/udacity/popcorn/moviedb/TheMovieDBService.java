package org.udacity.popcorn.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public final class TheMovieDBService {

    private final static String DISCOVER_REQUEST = "discover/movie";
    private final static String POPULAR_MOVIES_REQUEST = "sort_by=popularity.desc";

    private final static String API_KEY_TAG = "api_key";

    private final static String PREVIEW_SIZE = "w185";

    public interface TheMovieDBAPI {
        @GET("{version}/discover/movie?sort_by=popularity.desc")
        Call<Movies> getPopularMovies(
                @Path("version") String version,
                @Query(API_KEY_TAG) String apiKey
        );
    }
}
