package com.rsami.anuj.auth.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class reciptModel implements Serializable {
    private String userID;
    private String timestamp;
    private String movieNmae;
    private String date;
    private String Movietime;
    private List<String> seatsList;
    private int cost;
    private boolean provisional;

    public boolean isProvisional() {
        return provisional;
    }

    public void setProvisional(boolean provisional) {
        this.provisional = provisional;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMovietime() {
        return Movietime;
    }

    public void setMovietime(String movietime) {
        Movietime = movietime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMovieNmae() {
        return movieNmae;
    }

    public void setMovieNmae(String movieNmae) {
        this.movieNmae = movieNmae;
    }

    public List<String> getSeatsList() {
        return seatsList;
    }

    public void setSeatsList(List<String> seatsList) {
        this.seatsList = seatsList;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void getObject(String json) throws JSONException {
        if (json != null) {
            JSONObject properties = new JSONObject(json);
            // Extract out the title, number of people, and perceived strength values
            String userID = properties.getString("userID");
            String timestamp = properties.getString("timestamp");
            String movieNmae = properties.getString("movieNmae");
            String date = properties.getString("date");
            String Movietime = properties.getString("Movietime");
            List<String> list = new ArrayList<>();
            JSONArray jsonArray = properties.getJSONArray("seatsList");
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            int cost = properties.getInt("cost");
            // Create a new {@link Event} object

            boolean provisional = properties.getBoolean("provisional");

            this.setCost(cost);
            this.setSeatsList(list);
            this.setTimestamp(timestamp);
            this.setMovieNmae(movieNmae);
            this.setDate(date);
            this.setMovietime(Movietime);
            this.setUserID(userID);
            this.setProvisional(provisional);

        } else {
            this.setCost(0);
            this.setSeatsList(null);
            this.setTimestamp(null);
            this.setMovieNmae(null);
            this.setDate(null);
            this.setMovietime(null);
            this.setUserID(null);
            this.setProvisional(false);
        }
    }

}
