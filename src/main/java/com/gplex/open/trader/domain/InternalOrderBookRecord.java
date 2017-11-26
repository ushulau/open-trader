package com.gplex.open.trader.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

import static com.gplex.open.trader.utils.Utils.convert;

/**
 * Created by Vlad S. on 11/23/17.
 */
public class InternalOrderBookRecord extends OrderBookRecord{
    private String id;
    private Date date;
    private String status;
    private Double fillFee;

    public InternalOrderBookRecord(String side, Double price, Double size) {
        super(side, price, size);
    }

    public InternalOrderBookRecord(String side, String price, String size) {
        super(side, price, size);
    }

    public InternalOrderBookRecord(String side, double price, double size, String id, Date date, String status) {
        super(side, price, size);
        this.id = id;
        this.date = date;
        this.status = status;
    }

    public InternalOrderBookRecord(OrderResponse order) {
        super(order.getSide(), order.getPrice().doubleValue(), order.getSize().doubleValue());
        this.id = order.getId();
        this.date =  convert(order.getCreatedAt());
        this.status = order.getStatus();
        this.fillFee = order.getFillFees().doubleValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getFillFee() {
        return fillFee;
    }

    public void setFillFee(Double fillFee) {
        this.fillFee = fillFee;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
