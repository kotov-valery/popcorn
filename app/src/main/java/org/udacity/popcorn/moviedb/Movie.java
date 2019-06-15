package org.udacity.popcorn.moviedb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "movie_entity")
public class Movie implements Parcelable {
    @PrimaryKey
    public int id;
    public String title;
    public String poster_path;
    public String original_title;
    public String overview;
    public String release_date;
    public String vote_average;

    @ColumnInfo(name = "is_favorite")
    public int isFavorite;

    public static final int NOT_A_FAVORITE_MOVIE = 0;
    public static final int FAVORITE_MOVIE = 1;

    public Movie(int id, String title, String poster_path, String original_title,
                 String overview, String release_date, String vote_average, int isFavorite)
    {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.isFavorite = isFavorite;
    }

    @Ignore
    public Movie(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        poster_path = parcel.readString();
        original_title = parcel.readString();
        overview = parcel.readString();
        release_date = parcel.readString();
        vote_average = parcel.readString();
        isFavorite = parcel.readInt();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeInt(isFavorite);
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
