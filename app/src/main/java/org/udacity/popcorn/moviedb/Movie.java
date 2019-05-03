package org.udacity.popcorn.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public String title;
    public String poster_path;
    public String original_title;
    public String overview;
    public String release_date;
    public String vote_average;

    public Movie(String vTitle, String vPoster, String vOriginalTitle,
                 String vOverview, String vReleaseDate, String vVoteAverage)
    {
        title = vTitle;
        poster_path = vPoster;
        original_title = vOriginalTitle;
        overview = vOverview;
        release_date = vReleaseDate;
        vote_average = vVoteAverage;
    }

    public Movie(Parcel parcel) {
        title = parcel.readString();
        poster_path = parcel.readString();
        original_title = parcel.readString();
        overview = parcel.readString();
        release_date = parcel.readString();
        vote_average = parcel.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(vote_average);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
