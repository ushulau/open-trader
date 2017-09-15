package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vlad S. on 9/14/17.
 */
public class Order {
    private String price;
    private String size;
    private String side;
    @JsonProperty("product_id")
    private String productId;

    public Order() {
    }

    public Order(String productId, String side, Double price, Double size) {
        this.price = price.toString();
        this.size = size.toString();
        this.side = side;
        this.productId = productId;
    }

    public Order(String productId, String side, String price, String size) {
        this.price = price;
        this.size = size;
        this.side = side;
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
