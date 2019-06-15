package org.udacity.popcorn;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.udacity.popcorn.moviedb.Movie;
import org.udacity.popcorn.moviedb.Review;
import org.udacity.popcorn.moviedb.ReviewAdapter;
import org.udacity.popcorn.moviedb.TheMovieDB;
import org.udacity.popcorn.moviedb.TrailerAdapter;
import org.udacity.popcorn.utility.ImageLoader;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private int mMovieId;
    private TextView mOriginalTitle;
    private ImageView mPosterImage;
    private TextView mReleaseDate;
    private TextView mAverageRate;
    private TextView mMovieOverview;

    private RecyclerView mReviewsList;
    private ReviewAdapter mReviewAdapter;

    private TrailerAdapter mTrailerAdapter;

    public static final String MOVIE_OBJECT = "MovieObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mOriginalTitle = findViewById(R.id.tv_movie_original_title);
        mPosterImage = findViewById(R.id.img_poster_preview);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mAverageRate = findViewById(R.id.tv_average_vote);
        mMovieOverview = findViewById(R.id.tv_movie_overview);

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

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MOVIE_OBJECT)) {
            String movieJSON = intent.getStringExtra(MOVIE_OBJECT);
            Movie movie = (new Gson()).fromJson(movieJSON, Movie.class);
            mMovieId = movie.id;
            fillInDetails(movie);
            TheMovieDB.fetchMovieReviews(mMovieId, mReviewAdapter);
            TheMovieDB.fetchMovieTrailers(mMovieId, mTrailerAdapter);
        }
    }

    private void fillInDetails(Movie movie) {
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

    public void onAddMovieToFavoriets(View view) {
        Log.d("MovieDetailsActivity", "onAddMovieToFavoriets " + mMovieId);
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
