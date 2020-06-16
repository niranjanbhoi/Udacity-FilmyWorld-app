package com.developer.kimy.repository.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.developer.kimy.models.MovieModel;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM fav_movie")
    LiveData<List<MovieModel>> loadAllMovies();

    /**
     * this is the query to get all favorite
     * movies for app widget.
     * We can't use LiveData because there is no life cycle attached to
     * widget
     * @return returns the list of all favorite movie
     */
    @Query("SELECT * FROM fav_movie")
    List<MovieModel> loadAllMoviesForWidget();

    @Insert
    void insertMovie(MovieModel movie);

    @Delete
    void deleteMovie(MovieModel movie);

    @Query("SELECT * FROM fav_movie WHERE id = :id")
    LiveData<MovieModel> loadMovieById(int id);
}
