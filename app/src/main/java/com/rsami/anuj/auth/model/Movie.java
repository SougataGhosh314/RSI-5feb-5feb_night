package com.rsami.anuj.auth.model;

import java.io.Serializable;

public class Movie implements Serializable {
    String MovieName;
    String MovieDate;
    String MovieTime;
    String ImageURI;

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String movieName) {
        this.MovieName = movieName;
    }

    public void setMovieTime(String movieTime) {
        MovieTime = movieTime;
    }

    public String getMovieDate() {

        return MovieDate;
    }

    public void setMovieDate(String date) {
        MovieDate = date;
    }

    public String getMovieTime() {
        return MovieTime;
    }

    public String getImageURI() {
        return ImageURI;
    }

    public void setImageURI(String imageURI) {
        ImageURI = imageURI;
    }

}
