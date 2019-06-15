package org.udacity.popcorn.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class Trailers {
    @SerializedName("results")
    public ArrayList<Trailer> list;

    public Trailers(ArrayList<Trailer> vList) {
        list = vList;
    }

    public final int count() {
        return list.size();
    }

    public final Trailer get(int i) {
        return list.get(i);
    }
}
