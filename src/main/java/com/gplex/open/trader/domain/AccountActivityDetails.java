package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Vlad S. on 10/8/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountActivityDetails {
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("trade_id")
    private String tradeId;
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("order_id")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("order_id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("trade_id")
    public String getTradeId() {
        return tradeId;
    }

    @JsonProperty("trade_id")
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    @JsonProperty("product_id")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
