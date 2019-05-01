package org.udacity.popcorn.moviedb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.udacity.popcorn.R;
import org.udacity.popcorn.utility.ImageLoader;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Movies mMovies;
    private final OnPosterClickListener mListener;

    public interface OnPosterClickListener {
        void onClick(Movie movie);
    }

    public MovieAdapter(OnPosterClickListener listener) {
        mListener = listener;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {
        private final ImageView mImageView;

        MovieViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.img_movie_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mListener.onClick(mMovies.get(pos));
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
        ImageLoader.fetchPosterIntoView(mMovies.get(i).poster_path, movieViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.count();
    }

    public void setMovies(Movies movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}
