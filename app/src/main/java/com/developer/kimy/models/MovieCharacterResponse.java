package com.developer.kimy.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MovieCharacterResponse implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private List<MovieCharacters> cast;

    public MovieCharacterResponse(int id, List<MovieCharacters> cast) {
        this.id = id;
        this.cast = cast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieCharacters> getCast() {
        return cast;
    }

    public void setCast(List<MovieCharacters> cast) {
        this.cast = cast;
    }
}
