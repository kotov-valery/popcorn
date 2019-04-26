package org.udacity.popcorn.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.udacity.popcorn.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public MovieViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_movie_name);
        }

        public void setText(String text) {
            mTextView.setText(text);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_preview, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        String[] moviesList = TheMovieDB.getPopularMovies();
        movieViewHolder.setText(moviesList[i]);
    }

    @Override
    public int getItemCount() {
        return TheMovieDB.getItemCount();
    }
}
