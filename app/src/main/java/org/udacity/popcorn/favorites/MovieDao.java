package org.udacity.popcorn.favorites;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.udacity.popcorn.moviedb.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie_entity")
    LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM movie_entity WHERE id LIKE :id LIMIT 1")
    LiveData<Movie> loadMovieById(int id);

    @Insert
    void batchInsert(Movie... movies);

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie taskEntry);

    @Delete
    void deleteMovie(Movie movie);
}