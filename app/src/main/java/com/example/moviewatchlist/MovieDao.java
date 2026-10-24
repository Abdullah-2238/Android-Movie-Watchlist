package com.example.moviewatchlist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie movie);
    @Delete
    void delete(Movie movie);
    @Update
    void update(Movie movie);
    @Query("SELECT * FROM watchlist")
    LiveData<List<Movie>> getAllMovies();
}
