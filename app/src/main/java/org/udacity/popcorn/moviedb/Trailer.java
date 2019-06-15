package org.udacity.popcorn.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    public String name;
    public String site;
    public String key;

    public Trailer(String vName, String vSite, String vKey)
    {
        name = vName;
        site = vSite;
        key = vKey;
    }

    public Trailer(Parcel parcel) {
        name = parcel.readString();
        site = parcel.readString();
        key = parcel.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(key);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
