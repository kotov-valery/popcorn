package org.udacity.popcorn;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.udacity.popcorn.databinding.ActivityMovieDetailsBinding;
import org.udacity.popcorn.favorites.DetailsViewModel;
import org.udacity.popcorn.favorites.MovieDatabase;
import org.udacity.popcorn.moviedb.Movie;
import org.udacity.popcorn.moviedb.Review;
import org.udacity.popcorn.moviedb.ReviewAdapter;
import org.udacity.popcorn.moviedb.TheMovieDB;
import org.udacity.popcorn.moviedb.TrailerAdapter;
import org.udacity.popcorn.utility.AppExecutors;
import org.udacity.popcorn.utility.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private int mMovieId;

    private ActivityMovieDetailsBinding mBinding;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    private String mPosterUrl;
    private String mOriginalTitle;
    private String mOriginalReleaseDate;
    private boolean mIsFavoriteMovie;

    public static final String MOVIE_OBJECT = "MovieObject";

    private MovieDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        RecyclerView.LayoutManager reviewsLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.reviewsList.setLayoutManager(reviewsLayoutManager);

        mReviewAdapter = new ReviewAdapter(new OpenReviewInABrowser());
        mBinding.reviewsList.setAdapter(mReviewAdapter);

        TextView trailersLabel = findViewById(R.id.tv_trailers_label);
        View trailer1 = findViewById(R.id.trailer_placeholder1);
        View trailer2 = findViewById(R.id.trailer_placeholder2);
        View trailer3 = findViewById(R.id.trailer_placeholder3);

        mTrailerAdapter = new TrailerAdapter(trailersLabel,
                trailer1, trailer2, trailer3);

        mDb = MovieDatabase.getInstance(getApplicationContext());

        Movie movie = null;
        boolean reviewsWereRestored = false;
        boolean trailersWereRestored = false;
        if (savedInstanceState != null) {
            if(savedInstanceState.containsKey(MOVIE_OBJECT)) {
                movie = savedInstanceState.getParcelable(MOVIE_OBJECT);
            }
            if (mTrailerAdapter.hasSavedState(savedInstanceState)) {
                mTrailerAdapter.restoreStateFrom(savedInstanceState);
                trailersWereRestored = true;
            }
            if (mReviewAdapter.hasSavedState(savedInstanceState)) {
                mReviewAdapter.restoreStateFrom(savedInstanceState);
                reviewsWereRestored = true;
            }
       } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(MOVIE_OBJECT)) {
                String movieJSON = intent.getStringExtra(MOVIE_OBJECT);
                movie = (new Gson()).fromJson(movieJSON, Movie.class);
           }
        }

        if (movie == null) {
            Log.e(TAG, "Was not able to retrieve movie object");
            return;
        }

        mMovieId = movie.id;
        fromMovie(movie);

        subscribeToDbChanges(mMovieId);

        if (!trailersWereRestored) {
            TheMovieDB.fetchMovieTrailers(mMovieId, mTrailerAdapter);
        }

        if (!reviewsWereRestored) {
            TheMovieDB.fetchMovieReviews(mMovieId, mReviewAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_OBJECT, toMovie());
        mReviewAdapter.saveStateTo(outState);
        mTrailerAdapter.saveStateTo(outState);
        super.onSaveInstanceState(outState);
    }

    private void subscribeToDbChanges(final int movieId) {
        DetailsViewModelFactory factory = new DetailsViewModelFactory(mDb, movieId);

        DetailsViewModel viewModel =
                ViewModelProviders.of(this, factory).get(DetailsViewModel.class);

        viewModel.getMovie()
                .observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movieEntry) {
                        Log.d(TAG, "Get entry from the db " + movieEntry);
                        updateUiState(movieEntry);
                    }
                });
    }

    private void updateUiState(Movie movie) {
        if (movie == null) {
            mIsFavoriteMovie = false;
        } else {
            mIsFavoriteMovie = (movie.isFavorite == Movie.FAVORITE_MOVIE);
        }

        mBinding.markMovieAsFavoriteBtn.
                setText(mIsFavoriteMovie ?
                        getString(R.string.removeFromFavorites) :
                        getString(R.string.markAsFavorite));
    }

    private Movie toMovie() {
        String original_title = mOriginalTitle;
        String title = original_title;
        String overview = mBinding.tvMovieOverview.getText().toString();
        String release_date = mOriginalReleaseDate;

        String vote_average = "";
        String[] splitVote = mBinding.tvAverageVote
                .getText().toString().split("/");
        if (splitVote.length > 0) {
            vote_average = splitVote[0];
        }

        int isFavorite = mIsFavoriteMovie ? Movie.FAVORITE_MOVIE
                : Movie.NOT_A_FAVORITE_MOVIE;

        return new Movie(mMovieId, title, mPosterUrl, original_title,
                overview, release_date, vote_average, isFavorite);
    }


    private void fromMovie(Movie movie) {
        if (movie == null) return;
        mPosterUrl = movie.poster_path;
        mOriginalTitle = movie.original_title;
        mOriginalReleaseDate = movie.release_date;

        ImageLoader.fetchPosterIntoView(movie.poster_path, mBinding.imgPosterPreview);

        String title = mOriginalTitle;
        String[] splitReleaseDate = movie.release_date.split("-");
        if (splitReleaseDate.length > 0) {
            title += " (" + splitReleaseDate[0] + ")";
        }
        mBinding.tvMovieOriginalTitle.setText(title);

        mBinding.tvMovieOverview.setText(movie.overview);

        if (movie.vote_average != null && !movie.vote_average.isEmpty()) {
            String averageVote = movie.vote_average;
            averageVote += "/10";
            mBinding.tvAverageVote.setText(averageVote);
        }

        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.releaseDateFormat));
        try {
            Date releaseDate = dateFormat.parse(movie.release_date);
            mBinding.tvReleaseDate.setText(DateFormat
                    .getDateInstance(DateFormat.MEDIUM)
                    .format(releaseDate));
        } catch (ParseException e) {
            Log.e(TAG, "Could not parse release date: " + movie.release_date);
            mBinding.tvReleaseDate.setText(getString(R.string.yearIsMissingError));
        }
    }

    public void triggerAddOrRemoveToFavorites(View view) {
        final Movie movieInfo = toMovie();

        int isFavorite = mIsFavoriteMovie ? Movie.NOT_A_FAVORITE_MOVIE
                : Movie.FAVORITE_MOVIE;
        movieInfo.isFavorite = isFavorite;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (movieInfo.isFavorite == Movie.NOT_A_FAVORITE_MOVIE) {
                    Log.d(TAG, "Removing " + movieInfo.title + " movie to favorites");
                    mDb.movieDao().deleteMovie(movieInfo);
                } else {
                    Log.d(TAG, "Adding " + movieInfo.title + " movie to favorites");
                    mDb.movieDao().insertMovie(movieInfo);
                }
            }
        });
    }

    private class OpenReviewInABrowser implements ReviewAdapter.OnReviewClickListener {
        @Override
        public void onClick(Review review) {
            Intent openReviewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.url));
            if (openReviewIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(openReviewIntent);
            } else {
                Log.d(TAG, "Couldn't call " + Uri.parse(review.url)
                        + ", no receiving apps installed!");
            }
        }
    }
}
