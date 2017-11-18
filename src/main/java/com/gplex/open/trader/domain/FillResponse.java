package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gplex.open.trader.utils.ParseDateDeserializer;
import com.gplex.open.trader.utils.ParseDateSerializer;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Vlad S. on 11/9/17.
 */
public class FillResponse {

    @JsonProperty("trade_id")
    private Integer tradeId;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("price")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal price;
    @JsonProperty("size")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal size;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("created_at")
    @JsonSerialize(using = ParseDateSerializer.class)
    @JsonDeserialize(using = ParseDateDeserializer.class)
    private LocalDateTime createdAt;
    @JsonProperty("liquidity")
    private String liquidity;
    @JsonProperty("fee")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal fee;
    @JsonProperty("settled")
    private Boolean settled;
    @JsonProperty("side")
    private String side;

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(String liquidity) {
        this.liquidity = liquidity;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
