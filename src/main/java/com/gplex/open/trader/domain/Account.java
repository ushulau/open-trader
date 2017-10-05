package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vlad S. on 10/4/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    @JsonProperty("id")
    private String id;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("balance")
    private String balance;
    @JsonProperty("available")
    private String available;
    @JsonProperty("hold")
    private String hold;
    @JsonProperty("profile_id")
    private String profileId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
