package org.udacity.popcorn.moviedb;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.udacity.popcorn.R;
import org.udacity.popcorn.utility.ViewVideoHelper;

import java.util.ArrayList;

public class TrailerAdapter {
    private static final String YOUTUBE_ID = "YouTube";

    private static final String TRAILERS_STATE = "TrailersState";

    private TextView mTrailersLabel;

    private View mTrailer1;
    private View mTrailer2;
    private View mTrailer3;

    private TrailerClickListener mTrailer1ClickListener;
    private TrailerClickListener mTrailer2ClickListener;
    private TrailerClickListener mTrailer3ClickListener;

    private ArrayList<Trailer> mTrailersInfo;

    private class TrailerClickListener implements View.OnClickListener {
        private Trailer trailer;
        private Context context;

        public TrailerClickListener(Context vContext) {
            context = vContext;
        }

        public void setTrailer(Trailer vTrailer) {
            trailer = vTrailer;
        }

        @Override
        public void onClick(View v) {
            if (trailer == null) return;

            if (trailer.site.equals(YOUTUBE_ID)) {
                ViewVideoHelper.watchYouTubeVideo(context, trailer.key);
            }
        }
    }

    public TrailerAdapter(TextView label, View trailer1,
                          View trailer2, View trailer3)
    {
        mTrailersLabel = label;
        mTrailer1 = trailer1;
        mTrailer2 = trailer2;
        mTrailer3 = trailer3;

        hideTrailers();

        Context context = label.getContext();
        mTrailer1ClickListener = new TrailerClickListener(context);
        mTrailer2ClickListener = new TrailerClickListener(context);
        mTrailer3ClickListener = new TrailerClickListener(context);

        mTrailer1.setOnClickListener(mTrailer1ClickListener);
        mTrailer2.setOnClickListener(mTrailer2ClickListener);
        mTrailer3.setOnClickListener(mTrailer3ClickListener);

        mTrailersInfo = new ArrayList<>();
   }

    public void hideTrailers() {
        mTrailersLabel.setVisibility(View.GONE);
        mTrailer1.setVisibility(View.GONE);
        mTrailer2.setVisibility(View.GONE);
        mTrailer3.setVisibility(View.GONE);
    }

    public void updateInfo(Trailers trailers) {
        mTrailersInfo.clear();
        if (trailers.count() > 0) {
            Trailer trailer1 = trailers.get(0);
            mTrailersLabel.setVisibility(View.VISIBLE);
            TextView name = mTrailer1.findViewById(R.id.tv_trailer_name);
            name.setText(trailer1.name);
            mTrailer1.setVisibility(View.VISIBLE);
            mTrailer1ClickListener.setTrailer(trailer1);
            mTrailersInfo.add(trailer1);
        }
        if (trailers.count() > 1) {
            Trailer trailer2 = trailers.get(1);
            TextView name = mTrailer2.findViewById(R.id.tv_trailer_name);
            name.setText(trailer2.name);
            mTrailer2.setVisibility(View.VISIBLE);
            mTrailer2ClickListener.setTrailer(trailer2);
            mTrailersInfo.add(trailer2);
        }
        if (trailers.count() > 2) {
            Trailer trailer3 = trailers.get(2);
            TextView name = mTrailer3.findViewById(R.id.tv_trailer_name);
            name.setText(trailer3.name);
            mTrailer3.setVisibility(View.VISIBLE);
            mTrailer3ClickListener.setTrailer(trailer3);
            mTrailersInfo.add(trailer3);
        }
    }

    public boolean hasSavedState(Bundle state) {
        return state.containsKey(TRAILERS_STATE);
    }

    public void saveStateTo(Bundle state) {
        state.putParcelableArrayList(TRAILERS_STATE, mTrailersInfo);
    }

    public void restoreStateFrom(Bundle state) {
        mTrailersInfo = new ArrayList<>();
        ArrayList<Trailer> trailersInfo = state.getParcelableArrayList(TRAILERS_STATE);
        Trailers trailers = new Trailers(trailersInfo);
        updateInfo(trailers);
    }
}
