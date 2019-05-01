package org.udacity.popcorn.utility;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.udacity.popcorn.R;

public class ImageLoader {

    private static final String BASE_URL = "http://image.tmdb.org/t/p";
    private static final String RESOLUTION = "w500";

    public static void fetchPosterIntoView(String posterPath, ImageView view) {
        String url = BASE_URL + "/" + RESOLUTION + "/" + posterPath;
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.user_placeholder_loading)
                .error(R.drawable.user_placeholder_loading_error)
                .into(view);
    }
}
