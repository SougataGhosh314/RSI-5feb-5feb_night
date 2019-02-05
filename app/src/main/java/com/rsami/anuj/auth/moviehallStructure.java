package com.rsami.anuj.auth;

public class moviehallStructure {
    public String UID, seatno, timestamp, status;

    public moviehallStructure(String UID, String seatno, String timestamp, String status) {
        this.UID = UID;
        this.seatno = seatno;
        this.timestamp = timestamp;
        this.status = status;
    }

    public moviehallStructure() {
    }
}
