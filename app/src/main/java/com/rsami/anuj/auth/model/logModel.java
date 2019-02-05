package com.rsami.anuj.auth.model;

import java.io.Serializable;

public class logModel implements Serializable {

    public String date,seats,time,rsiID,totalCost,dependents,guest,member,movieName,typeOfTicket,pno,dcount_lim;


    public logModel(String date, String seats, String time, String rsiID, String totalCost, String dependents, String guest, String member, String movieName, String typeOfTicket,String pno, String dcount_lim) {
        this.date = date;
        this.seats = seats;
        this.time = time;
        this.rsiID = rsiID;
        this.totalCost = totalCost;
        this.dependents = dependents;
        this.guest = guest;
        this.member = member;
        this.movieName = movieName;
        this.typeOfTicket = typeOfTicket;
        this.pno = pno;
        this.dcount_lim = dcount_lim;
    }

    public logModel() {

    }



}
