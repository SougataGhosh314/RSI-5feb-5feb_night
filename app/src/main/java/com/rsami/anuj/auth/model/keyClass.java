package com.rsami.anuj.auth.model;

public class keyClass {
    public String prv,pbl,smsapi;

    public keyClass(String prv, String pbl, String smsapi) {
        this.prv = prv;
        this.pbl = pbl;
        this.smsapi = smsapi;
    }

    public keyClass() {
    }

    public String getPrv() {
        return prv;
    }

    public void setPrv(String prv) {
        this.prv = prv;
    }

    public String getPlb() {
        return pbl;
    }

    public void setPlb(String plb) {
        this.pbl = plb;
    }

    public String getSmsapi() {
        return smsapi;
    }

    public void setSmsapi(String smsapi) {
        this.smsapi = smsapi;
    }
}
