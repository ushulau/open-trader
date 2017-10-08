package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Vlad S. on 10/8/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountActivity {
    @JsonProperty("id")
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("balance")
    private String balance;
    @JsonProperty("type")
    private String type;
    @JsonProperty("details")
    private AccountActivityDetails details;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("balance")
    public String getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(String balance) {
        this.balance = balance;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("details")
    public AccountActivityDetails getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(AccountActivityDetails details) {
        this.details = details;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
