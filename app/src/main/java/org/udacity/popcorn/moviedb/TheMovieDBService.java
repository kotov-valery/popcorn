package org.udacity.popcorn.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

final class TheMovieDBService {

    private final static String API_KEY_TAG = "api_key";
    private final static String API_SORT_BY_TAG = "sort_by";

    public final static String SORT_BY_POPULARITY = "popular";
    public final static String SORT_BY_VOTES = "top_rated";

    public interface TheMovieDBAPI {
        @GET("{version}/movie/{" + API_SORT_BY_TAG + "}")
        Call<Movies> fetchMovies(
                @Path("version") String version,
                @Path(API_SORT_BY_TAG) String sortBy,
                @Query(API_KEY_TAG) String apiKey
        );
    }
}
