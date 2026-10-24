package com.example.moviewatchlist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WatchlistActivity extends AppCompatActivity {
    ImageButton btnBack;
    RecyclerView rvWatchlist;
    TextView tvEmpty;
    MovieViewModel viewModel;
    MovieAdapter adapter;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_watchlist);
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        btnBack = findViewById(R.id.btnBack);
        rvWatchlist = findViewById(R.id.rvWatchlist);
        tvEmpty = findViewById(R.id.tvEmpty);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movies = new ArrayList<>();
        adapter = new MovieAdapter(this, movies, new OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                movie.setWatched(!movie.isWatched());
                viewModel.update(movie);
            }
        },
                new OnMovieLongClickListener() {
                    @Override
                    public void onMovieLongClick(Movie movie) {
                        showDeleteDialog(movie);
                    }
        });

        rvWatchlist.setLayoutManager(new LinearLayoutManager(this));
        rvWatchlist.setAdapter(adapter);
        viewModel.getWatchlist().observe(this, list -> {
                    adapter.setMovies(list);
                    if (list.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                    }
                });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showDeleteDialog(Movie movie) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Movie?")
                .setMessage("Are you sure you want to delete this movie?")
                .setPositiveButton("Delete", (dialog, which) -> viewModel.delete(movie))
                .setNegativeButton("Cancel", null)
                .show();
    }
}