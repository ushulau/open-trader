package com.gplex.open.trader.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Vlad S. on 11/16/17.
 */
public class OrderBookRecord {
    private String side;
    private Double price;
    private Double size;

    public OrderBookRecord(String side, Double price, Double size) {
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public OrderBookRecord(String side, String price, String size) {
        this.side = side;
        this.price = Double.valueOf(price);
        this.size = Double.valueOf(size);
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public boolean isBuy(){
        return StringUtils.isNotBlank(this.getSide()) && "buy".equalsIgnoreCase(this.getSide());
    }

    public boolean isSell(){
        return StringUtils.isNotBlank(this.getSide()) && "sell".equalsIgnoreCase(this.getSide());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
