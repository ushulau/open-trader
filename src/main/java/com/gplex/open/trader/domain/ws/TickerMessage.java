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
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("open_24h")
    private BigDecimal open24h;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("volume_24h")
    private BigDecimal volume24h;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("low_24h")
    private BigDecimal low24h;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("high_24h")
    private BigDecimal high24h;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("volume_30d")
    private BigDecimal volume30d;

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

    public BigDecimal getOpen24h() {
        return open24h;
    }

    public void setOpen24h(BigDecimal open24h) {
        this.open24h = open24h;
    }

    public BigDecimal getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(BigDecimal volume24h) {
        this.volume24h = volume24h;
    }

    public BigDecimal getLow24h() {
        return low24h;
    }

    public void setLow24h(BigDecimal low24h) {
        this.low24h = low24h;
    }

    public BigDecimal getHigh24h() {
        return high24h;
    }

    public void setHigh24h(BigDecimal high24h) {
        this.high24h = high24h;
    }

    public BigDecimal getVolume30d() {
        return volume30d;
    }

    public void setVolume30d(BigDecimal volume30d) {
        this.volume30d = volume30d;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
