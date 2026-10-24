package com.example.moviewatchlist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApiService {
    @GET("search/movie")
    Call<MovieResponse> getMovie(
            @Query("api_key") String apiKey,
            @Query("query") String movieName
    );
}
