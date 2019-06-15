package org.udacity.popcorn.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class Reviews {
    @SerializedName("results")
    public ArrayList<Review> list;

    public Reviews(ArrayList<Review> vList) {
        list = vList;
    }

    public final int count() {
        return list.size();
    }

    public final Review get(int i) {
        return list.get(i);
    }
}
