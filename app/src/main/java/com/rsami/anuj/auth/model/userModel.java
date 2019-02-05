package com.rsami.anuj.auth.model;

import java.io.Serializable;

public class userModel implements Serializable {
    public String mobno, pass, rsiID;

    public userModel(String mobno, String pass, String rsiID) {
        this.mobno = mobno;
        this.pass = pass;
        this.rsiID = rsiID;
    }

    public userModel() {
    }
}