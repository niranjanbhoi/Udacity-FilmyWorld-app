package com.developer.kimy.utils;

public class YouTubePath {
    /**
     * This method takes the string of video id and build
     * a url string for
     * @param videoId for which we want thumbnail
     * @return url to get video of movie
     */
    public static String buildVideoPath(String videoId){
        return "http://www.youtube.com/watch?v=" + videoId;
    }

    public static String buildAppVideoPath(String videoId){
        return "vnd.youtube:" + videoId;
    }
}
