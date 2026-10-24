package com.example.moviewatchlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    ImageButton btnSearch;
    TextView tvEmpty;
    Button btnWatchlist;
    RecyclerView rvMovies;
    MovieViewModel viewModel;
    MovieAdapter adapter;
    List<Movie> movies;

    Set<Integer> savedIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnWatchlist = findViewById(R.id.btnWatchlist);
        rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();
        savedIds = new HashSet<>();

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        adapter = new MovieAdapter(this, movies, new OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                if (savedIds.contains(movie.getId())) {
                    Snackbar.make(rvMovies, "Already in your watchlist", Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.insert(movie);
                    Snackbar.make(rvMovies, movie.getTitle() + " added", Snackbar.LENGTH_LONG)
                            .setAction("Undo", v -> viewModel.delete(movie))
                            .show();
                }
            }
        },
                new OnMovieLongClickListener() {
                    @Override
                    public void onMovieLongClick(Movie movie) {
                        // nothing on this screen
                    }
        });

        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);
        viewModel.getSearchResults().observe(this, list -> {
            adapter.setMovies(list);
            if (list.isEmpty()) {
                tvEmpty.setText("No movies found for:" + etSearch.getText().toString().trim());
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                tvEmpty.setVisibility(View.GONE);
            }
        });

        btnSearch.setOnClickListener(v -> performSearch());

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        btnWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WatchlistActivity.class);
                startActivity(intent);
            }
        });

        viewModel.getWatchlist().observe(this, list -> {
            savedIds = new HashSet<>();
            for (Movie m : list) {
                savedIds.add(m.getId());
            }
            adapter.setSavedIds(savedIds);
        });

    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a movie name", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.searchMovies(query);
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        etSearch.clearFocus();
    }
}