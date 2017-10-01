package com.gplex.open.trader.domain;

/**
 * Created by Vlad S. on 9/15/17.
 */
public class TimeResponse {


    private String iso;
    private Double epoch;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Double getEpoch() {
        return epoch;
    }

    public void setEpoch(Double epoch) {
        this.epoch = epoch;
    }
}
