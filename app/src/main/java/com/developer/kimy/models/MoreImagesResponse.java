package com.developer.kimy.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MoreImagesResponse implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("backdrops")
    private List<MoreImagesModel> backdrops;
    @SerializedName("posters")
    private List<MoreImagesModel> poster;

    public MoreImagesResponse(int id, List<MoreImagesModel> backdrops, List<MoreImagesModel> poster) {
        this.id = id;
        this.backdrops = backdrops;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MoreImagesModel> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<MoreImagesModel> backdrops) {
        this.backdrops = backdrops;
    }

    public List<MoreImagesModel> getPoster() {
        return poster;
    }

    public void setPoster(List<MoreImagesModel> poster) {
        this.poster = poster;
    }
}
