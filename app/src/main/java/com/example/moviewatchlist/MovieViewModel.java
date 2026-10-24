package com.example.moviewatchlist;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getWatchlist() {
        return repository.getWatchlist();
    }
    public LiveData<List<Movie>> getSearchResults() {
        return repository.getSearchResults();
    }
    public void searchMovies(String query) {
        repository.searchMovies(query);
    }
    public void insert(Movie movie) {
        repository.insert(movie);
    }
    public void update(Movie movie) {
        repository.update(movie);
    }
    public void delete(Movie movie) {
        repository.delete(movie);
    }
}
