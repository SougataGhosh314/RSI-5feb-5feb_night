package com.rsami.anuj.auth.model;

public class MovieContent {

    private String name;
    private String date;
    private String image_url;
    private String timing;

    private String duration;
    private String language;
    private String certification;
    private String trailer;
    private String available_seats;

    public MovieContent() {
    }

    public MovieContent(String name, String date, String image_url, String timing, String duration, String language, String certification, String trailer, String available_seats) {
        this.name = name;
        this.date = date;
        this.image_url = image_url;
        this.timing = timing;
        this.duration = duration;
        this.language = language;
        this.certification = certification;
        this.trailer = trailer;
        this.available_seats = available_seats;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(String available_seats) {
        this.available_seats = available_seats;
    }
}
