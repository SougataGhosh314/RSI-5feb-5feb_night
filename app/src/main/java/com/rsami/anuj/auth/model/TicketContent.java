package com.rsami.anuj.auth.model;

import java.util.List;

public class TicketContent {
    private int cost;
    private String date;
    private String movieNmae;
    private String movietime;
    private List<String> seatsList;
    private String timestamp;
    private String userID;
    private boolean provisional;

    public TicketContent() {
    }

    public TicketContent(int cost, String date, String movieNmae, String movietime, List<String> seatsList, String timestamp, String userID, boolean provisional) {
        this.cost = cost;
        this.date = date;
        this.movieNmae = movieNmae;
        this.movietime = movietime;
        this.seatsList = seatsList;
        this.timestamp = timestamp;
        this.userID = userID;
        this.provisional = provisional;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMovieNmae() {
        return movieNmae;
    }

    public void setMovieNmae(String movieNmae) {
        this.movieNmae = movieNmae;
    }

    public String getMovietime() {
        return movietime;
    }

    public void setMovietime(String movietime) {
        this.movietime = movietime;
    }

    public List<String> getSeatsList() {
        return seatsList;
    }

    public void setSeatsList(List<String> seatsList) {
        this.seatsList = seatsList;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isProvisional() {
        return provisional;
    }

    public void setProvisional(boolean provisional) {
        this.provisional = provisional;
    }
}
