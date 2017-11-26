package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gplex.open.trader.constant.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Vlad S. on 9/14/17.
 */
public class Order {
    private String price;
    private String size;
    private String side;
    @JsonProperty("product_id")
    private String productId;
    private String type;
    @JsonProperty("post_only")
    private Boolean postOnly;


    public Order() {
    }

    public Order(String productId, String side, Double price, Double size) {
        this.price = price.toString();
        this.size = size.toString();
        this.side = side;
        this.productId = productId;
        this.type = Const.OrderType.LIMIT;
        this.postOnly = true;
    }

    public Order(String productId, String side, String price, String size) {
        this.price = price;
        this.size = size;
        this.side = side;
        this.productId = productId;
        this.type = Const.OrderType.LIMIT;
        this.postOnly = true;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBuy(){
        return StringUtils.isNotBlank(this.getSide()) && "buy".equalsIgnoreCase(this.getSide());
    }

    public boolean isSell(){
        return StringUtils.isNotBlank(this.getSide()) && "sell".equalsIgnoreCase(this.getSide());
    }

    public Boolean isPostOnly() {
        return postOnly;
    }

    public void setPostOnly(Boolean postOnly) {
        this.postOnly = postOnly;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
