package org.udacity.popcorn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.udacity.popcorn.data.MovieAdapter;
import org.udacity.popcorn.data.TheMovieDB;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Popcorn" + MainActivity.class.getSimpleName();

    private RecyclerView mMoviesPreview;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPreview = findViewById(R.id.thumbnails_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mMoviesPreview.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter();
        mMoviesPreview.setAdapter(mAdapter);
    }
}
