package com.gplex.open.trader.domain.ws;

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
 * Created by Vlad S. on 11/4/17.
 */
public class TickerMessage {
    @JsonProperty("type")
    private String type;
    @JsonProperty("trade_id")
    private Long tradeId;
    @JsonProperty("sequence")
    private Long sequence;
    @JsonProperty("time")
    @JsonSerialize(using = ParseDateSerializer.class)
    @JsonDeserialize(using = ParseDateDeserializer.class)
    private LocalDateTime time;
    @JsonProperty("product_id")
    private String productId;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("side")
    private String side;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("last_size")
    private BigDecimal lastSize;
    @JsonProperty("best_bid")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal bestBid;
    @JsonProperty("best_ask")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal bestAsk;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("trade_id")
    public Long getTradeId() {
        return tradeId;
    }

    @JsonProperty("trade_id")
    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    @JsonProperty("sequence")
    public Long getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @JsonProperty("product_id")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("price")
    public BigDecimal getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @JsonProperty("side")
    public String getSide() {
        return side;
    }

    @JsonProperty("side")
    public void setSide(String side) {
        this.side = side;
    }

    @JsonProperty("last_size")
    public BigDecimal getLastSize() {
        return lastSize;
    }

    @JsonProperty("last_size")
    public void setLastSize(BigDecimal lastSize) {
        this.lastSize = lastSize;
    }

    @JsonProperty("best_bid")
    public BigDecimal getBestBid() {
        return bestBid;
    }

    @JsonProperty("best_bid")
    public void setBestBid(BigDecimal bestBid) {
        this.bestBid = bestBid;
    }

    @JsonProperty("best_ask")
    public BigDecimal getBestAsk() {
        return bestAsk;
    }

    @JsonProperty("best_ask")
    public void setBestAsk(BigDecimal bestAsk) {
        this.bestAsk = bestAsk;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
