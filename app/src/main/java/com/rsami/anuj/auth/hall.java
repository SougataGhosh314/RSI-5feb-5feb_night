package com.rsami.anuj.auth;

public class hall {
    public String seatno, dateReserved, user, timestamp, status;

    public hall(String seatno, String dateReserved, String user, String timestamp, String status) {
        this.seatno = seatno;
        this.dateReserved = dateReserved;
        this.user = user;
        this.timestamp = timestamp;
        this.status = status;
    }

    public hall() {
    }
}