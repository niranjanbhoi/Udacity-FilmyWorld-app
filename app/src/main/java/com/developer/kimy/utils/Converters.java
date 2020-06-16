package com.developer.kimy.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class Converters implements Serializable {

    @TypeConverter
    public String fromGenreList(List<Integer> genreList){
        if (genreList == null){
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>(){}.getType();

        return gson.toJson(genreList, type);
    }

    @TypeConverter
    public List<Integer> toGenreList(String genreString){
        if (genreString == null){
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>(){}.getType();

        return gson.fromJson(genreString, type);
    }
}
