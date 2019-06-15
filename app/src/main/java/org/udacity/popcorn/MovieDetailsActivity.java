package org.udacity.popcorn;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.udacity.popcorn.favorites.DetailsViewModel;
import org.udacity.popcorn.favorites.MovieDatabase;
import org.udacity.popcorn.moviedb.Movie;
import org.udacity.popcorn.moviedb.Review;
import org.udacity.popcorn.moviedb.ReviewAdapter;
import org.udacity.popcorn.moviedb.TheMovieDB;
import org.udacity.popcorn.moviedb.TrailerAdapter;
import org.udacity.popcorn.utility.AppExecutors;
import org.udacity.popcorn.utility.ImageLoader;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private int mMovieId;
    private TextView mOriginalTitle;
    private ImageView mPosterImage;
    private TextView mReleaseDate;
    private TextView mAverageRate;
    private TextView mMovieOverview;

    private Button mMarkAsFavorite;

    private RecyclerView mReviewsList;
    private ReviewAdapter mReviewAdapter;

    private TrailerAdapter mTrailerAdapter;

    private String mPosterUrl;
    private boolean mIsFavoriteMovie;

    public static final String MOVIE_OBJECT = "MovieObject";

    private MovieDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mOriginalTitle = findViewById(R.id.tv_movie_original_title);
        mPosterImage = findViewById(R.id.img_poster_preview);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mAverageRate = findViewById(R.id.tv_average_vote);
        mMovieOverview = findViewById(R.id.tv_movie_overview);

        mMarkAsFavorite = findViewById(R.id.mark_movie_as_favorite_btn);

        mReviewsList = findViewById(R.id.reviews_list);
        RecyclerView.LayoutManager reviewsLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviewsList.setLayoutManager(reviewsLayoutManager);

        mReviewAdapter = new ReviewAdapter(new OpenReviewInABrowser());
        mReviewsList.setAdapter(mReviewAdapter);

        TextView trailersLabel = findViewById(R.id.tv_trailers_label);
        View trailer1 = findViewById(R.id.trailer_placeholder1);
        View trailer2 = findViewById(R.id.trailer_placeholder2);
        View trailer3 = findViewById(R.id.trailer_placeholder3);

        mTrailerAdapter = new TrailerAdapter(trailersLabel,
                trailer1, trailer2, trailer3);

        mDb = MovieDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MOVIE_OBJECT)) {
            String movieJSON = intent.getStringExtra(MOVIE_OBJECT);
            Movie movie = (new Gson()).fromJson(movieJSON, Movie.class);

            mMovieId = movie.id;
            fillInDetails(movie);

            subscribeToDbChanges(mMovieId);

            TheMovieDB.fetchMovieReviews(mMovieId, mReviewAdapter);
            TheMovieDB.fetchMovieTrailers(mMovieId, mTrailerAdapter);
        }
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

        mMarkAsFavorite.setText(mIsFavoriteMovie ? getString(R.string.removeFromFavorites) :
                getString(R.string.markAsFavorite));
    }

    private void fillInDetails(Movie movie) {
        mPosterUrl = movie.poster_path;
        ImageLoader.fetchPosterIntoView(movie.poster_path, mPosterImage);

        mOriginalTitle.setText(movie.original_title);
        mMovieOverview.setText(movie.overview);

        if (movie.vote_average != null && !movie.vote_average.isEmpty()) {
            String averageVote = movie.vote_average;
            averageVote += "/10";
            mAverageRate.setText(averageVote);
        }

        String releaseYear;
        String[] splitRelease = movie.release_date.split("-");
        if (splitRelease.length > 0) {
            releaseYear = splitRelease[0];
        } else {
            releaseYear = getString(R.string.yearIsMissingError);
        }
        mReleaseDate.setText(releaseYear);
    }

    public void triggerAddOrRemoveToFavorites(View view) {
        String title = mOriginalTitle.getText().toString();
        String original_title = title;
        String overview = mMovieOverview.getText().toString();
        String release_date = mReleaseDate.getText().toString();
        String vote_average = mAverageRate.getText().toString();
        int isFavorite = mIsFavoriteMovie ? Movie.NOT_A_FAVORITE_MOVIE
                : Movie.FAVORITE_MOVIE;

        final Movie movieInfo = new Movie(mMovieId, title, mPosterUrl, original_title,
                overview, release_date, vote_average, isFavorite);

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
