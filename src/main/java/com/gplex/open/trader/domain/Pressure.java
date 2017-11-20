package com.gplex.open.trader.domain;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by Vlad S. on 11/19/17.
 */
public class Pressure {
    private List<Pair<Double, Double>> force;
    private double buy;
    private double sell;
    private List<Double> diff;

    public Pressure(double buy, double sell, List<Double> diff) {
        this.buy = buy;
        this.sell = sell;
        this.diff = diff;

    }

    public Pressure(double buy, double sell, List<Double> diff, List<Pair<Double, Double>> force) {
        this.buy = buy;
        this.sell = sell;
        this.diff = diff;
        this.force = force;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public List<Double> getDiff() {
        return diff;
    }

    public void setDiff(List<Double> diff) {
        this.diff = diff;
    }

    public List<Pair<Double, Double>> getForce() {
        return force;
    }

    public void setForce(List<Pair<Double, Double>> force) {
        this.force = force;
    }
}
