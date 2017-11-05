package com.gplex.open.trader.domain;

import com.gplex.open.trader.domain.ws.TickerMessage;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Vlad S. on 11/4/17.
 */
public class Candle {

    private Long time;
    private Double low;
    private Double high;
    private Double open;
    private Double close;
    private Double volume;

    public Candle() {

    }

    public Candle(Long time, Double low, Double high, Double open, Double close, Double volume) {
        this.time = time;
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
        this.volume = volume;
    }

    public Candle(Long time, TickerMessage tm){
        this.time = time;
        this.low = tm.getPrice().doubleValue();
        this.high = tm.getPrice().doubleValue();
        this.open = tm.getPrice().doubleValue();
        this.close = tm.getPrice().doubleValue();
        this.volume = tm.getLastSize().doubleValue();

    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public void add(TickerMessage tm) {
        double v = tm.getPrice().doubleValue();
        this.high =Math.max(this.high, v);
        this.low =Math.min(this.high, v);
        this.close = v;
        this.volume += tm.getLastSize().doubleValue();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
