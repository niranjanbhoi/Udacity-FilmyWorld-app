package com.developer.kimy.utils;

/**
 * This class is used to create image path
 */
public class ImagePath {
    public static String movieImagePathBuilder(String imagePath) {
        return "https://image.tmdb.org/t/p/" +
                "w500" +
                imagePath;
    }

}
