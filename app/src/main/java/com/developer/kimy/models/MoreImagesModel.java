package com.developer.kimy.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MoreImagesModel implements Serializable {
    @SerializedName("aspect_ratio")
    private Double aspectRatio;
    @SerializedName("file_path")
    private String filePath;
    @SerializedName("height")
    private int height;
    @SerializedName("iso_639_1")
    private String iso;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("width")
    private int width;

    public MoreImagesModel(Double aspectRatio, String filePath, int height, String iso, Double voteAverage, int voteCount, int width) {
        this.aspectRatio = aspectRatio;
        this.filePath = filePath;
        this.height = height;
        this.iso = iso;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.width = width;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
