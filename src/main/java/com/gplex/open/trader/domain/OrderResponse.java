package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gplex.open.trader.utils.ParseDateDeserializer;
import com.gplex.open.trader.utils.ParseDateSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Vlad S. on 11/5/17.
 */
public class OrderResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("price")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal price;
    @JsonProperty("size")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal size;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("side")
    private String side;
    @JsonProperty("stp")
    private String stp;
    @JsonProperty("type")
    private String type;
    @JsonProperty("time_in_force")
    private String timeInForce;
    @JsonProperty("post_only")
    private Boolean postOnly;
    @JsonProperty("created_at")
    @JsonSerialize(using = ParseDateSerializer.class)
    @JsonDeserialize(using = ParseDateDeserializer.class)
    private LocalDateTime createdAt;
    @JsonProperty("fill_fees")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal fillFees;
    @JsonProperty("filled_size")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal filledSize;
    @JsonProperty("executed_value")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal executedValue;
    @JsonProperty("status")
    private String status;
    @JsonProperty("settled")
    private Boolean settled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getStp() {
        return stp;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public Boolean getPostOnly() {
        return postOnly;
    }

    public void setPostOnly(Boolean postOnly) {
        this.postOnly = postOnly;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getFillFees() {
        return fillFees;
    }

    public void setFillFees(BigDecimal fillFees) {
        this.fillFees = fillFees;
    }

    public BigDecimal getFilledSize() {
        return filledSize;
    }

    public void setFilledSize(BigDecimal filledSize) {
        this.filledSize = filledSize;
    }

    public BigDecimal getExecutedValue() {
        return executedValue;
    }

    public void setExecutedValue(BigDecimal executedValue) {
        this.executedValue = executedValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public boolean isBuy(){
        return StringUtils.isNotBlank(this.getSide()) && "buy".equalsIgnoreCase(this.getSide());
    }

    public boolean isSell(){
        return StringUtils.isNotBlank(this.getSide()) && "sell".equalsIgnoreCase(this.getSide());
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
