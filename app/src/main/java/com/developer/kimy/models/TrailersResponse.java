package com.developer.kimy.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TrailersResponse implements Serializable {
    @SerializedName("results")
    private List<TrailerModel> results;

    public TrailersResponse() {
    }

    public List<TrailerModel> getResults() {
        return results;
    }

    public void setResults(List<TrailerModel> results) {
        this.results = results;
    }
}
