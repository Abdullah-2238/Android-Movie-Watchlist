package com.example.moviewatchlist;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private MovieDao movieDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private LiveData<List<Movie>> watchlist;
    private TmdbApiService apiService;
    private MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();

    public MovieRepository(Application application) {
        movieDao = AppDatabase.getInstance(application).movieDao();
        watchlist = movieDao.getAllMovies();
        apiService = RetrofitClient.getInstance().create(TmdbApiService.class);
    }

    public LiveData<List<Movie>> getWatchlist() {
        return watchlist;
    }
    public LiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }
    public void insert(Movie movie) {
        executor.execute(() -> movieDao.insert(movie));
    }
    public void update(Movie movie) {
        executor.execute(() -> movieDao.update(movie));
    }
    public void delete(Movie movie) {
        executor.execute(() -> movieDao.delete(movie));
    }
    public void searchMovies(String query) {
        Call<MovieResponse> call = apiService.getMovie(BuildConfig.TMDB_API_KEY, query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieResponse result = response.body();
                    searchResults.setValue(result.getResults());
                }
                else { searchResults.setValue(new ArrayList<>()); }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                searchResults.setValue(new ArrayList<>());
            }
        });
    }

}