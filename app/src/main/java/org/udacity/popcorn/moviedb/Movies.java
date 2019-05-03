package org.udacity.popcorn.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class Movies {
    @SerializedName("results")
    public ArrayList<Movie> list;

    public Movies(ArrayList<Movie> vList) {
        list = vList;
    }

    public final int count() {
        return list.size();
    }

    public final Movie get(int i) {
        return list.get(i);
    }
}
