package org.udacity.popcorn.data;

public class TheMovieDB {
    private final static String[] mPopularMovies = {
            "Mr. Robot",
            "Chappie",
            "Mad Max",
            "Jurassic Word",
            "Dead Pool",
            "Thor",
            "Avengers"
    };

    public static final String[] getPopularMovies() {
        return mPopularMovies;
    }

    public static final int getItemCount() {
        return mPopularMovies.length;
    }
}
