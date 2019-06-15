package org.udacity.popcorn.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    public String author;
    public String content;
    public String url;

    public Review(String vAuthor, String vContent,
                  String vUrl)
    {
        author = vAuthor;
        content = vContent;
        url = vUrl;
    }

    public Review(Parcel parcel) {
        author = parcel.readString();
        content = parcel.readString();
        url = parcel.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
