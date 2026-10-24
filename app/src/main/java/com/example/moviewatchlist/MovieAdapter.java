package com.example.moviewatchlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    Context context;
    List<Movie> movies;
    OnMovieClickListener listener;
    OnMovieLongClickListener longClickListener;
    Set<Integer> savedIds = new HashSet<>();
    boolean showSaveStatus = false;

    public void setSavedIds(Set<Integer> ids) {
        this.savedIds = ids;
        this.showSaveStatus = true;
        notifyDataSetChanged();
    }

    public MovieAdapter(Context context, List<Movie> movies, OnMovieClickListener listener, OnMovieLongClickListener longClickListener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.tvTitle.setText(movie.getTitle());

        if (movie.getPosterPath() != null) {
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                    .into(holder.ivPoster);
        } else {
            holder.ivPoster.setImageDrawable(null);
        }

        String date = movie.getReleaseDate();
        if (date != null && date.length() >= 4) {
            holder.tvMeta.setText(date.substring(0, 4) + " · ★ " + movie.getVoteAverage());
        } else {
            holder.tvMeta.setText("★ " + movie.getVoteAverage());
        }

        holder.tvOverview.setText(movie.getOverview());

        if (movie.isWatched()) {
            holder.tvWatched.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.tvWatched.setVisibility(View.GONE);
            holder.itemView.setAlpha(1f);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMovieClick(movie);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onMovieLongClick(movie);
                return true;
            }
        });


        if (!showSaveStatus) {
            holder.llSaveStatus.setVisibility(View.GONE);
        } else {
            holder.llSaveStatus.setVisibility(View.VISIBLE);
            if (savedIds.contains(movie.getId())) {
                holder.ivSaveIcon.setImageResource(R.drawable.ic_tick_double);
                holder.tvSaveText.setText("In Watchlist");
                holder.tvSaveText.setTextColor(Color.parseColor("#4CAF50"));
            } else {
                holder.ivSaveIcon.setImageResource(R.drawable.ic_tick_single);
                holder.tvSaveText.setText("Add to Watchlist");
                holder.tvSaveText.setTextColor(Color.parseColor("#888888"));
            }
        }
    }

    @Override
    public int getItemCount() { return movies.size(); }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoster;
        TextView tvTitle;
        TextView tvMeta;
        TextView tvOverview;
        TextView tvWatched;
        LinearLayout llSaveStatus;
        ImageView ivSaveIcon;
        TextView tvSaveText;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvWatched = itemView.findViewById(R.id.tvWatched);
            llSaveStatus = itemView.findViewById(R.id.llSaveStatus);
            ivSaveIcon = itemView.findViewById(R.id.ivSaveIcon);
            tvSaveText = itemView.findViewById(R.id.tvSaveText);
        }
    }
}