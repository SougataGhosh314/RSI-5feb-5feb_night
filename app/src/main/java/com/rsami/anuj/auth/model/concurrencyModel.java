package com.rsami.anuj.auth.model;

public class concurrencyModel {
    private String user,seat;
    private Long time;

    public concurrencyModel(String user, String seat, Long time) {
        this.user = user;
        this.seat = seat;
        this.time = time;
    }

    public concurrencyModel() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
