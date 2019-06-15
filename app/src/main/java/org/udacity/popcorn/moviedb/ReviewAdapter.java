package org.udacity.popcorn.moviedb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.udacity.popcorn.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> mReviews;
    private OnReviewClickListener mListener;

    public interface OnReviewClickListener {
        void onClick(Review review);
    }

    public ReviewAdapter(OnReviewClickListener listener) {
        mListener = listener;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {
        private final TextView mAuthor;
        private final TextView mContent;

        ReviewViewHolder(View view) {
            super(view);
            mAuthor = view.findViewById(R.id.tv_review_author);
            mContent = view.findViewById(R.id.tv_review_content);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mListener.onClick(mReviews.get(pos));
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_review_layout, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        view.setOnClickListener(reviewViewHolder);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        Review review = mReviews.get(i);
        reviewViewHolder.mAuthor.setText(review.author);
        reviewViewHolder.mContent.setText(review.content);
    }

    @Override
    public int getItemCount() {
        return mReviews == null ? 0 : mReviews.size();
    }

    public void setReviews(Reviews reviews) {
        mReviews = reviews.list;
        notifyDataSetChanged();
    }
}
