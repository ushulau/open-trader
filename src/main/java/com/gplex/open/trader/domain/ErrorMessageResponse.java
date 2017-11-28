package com.gplex.open.trader.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Vlad S. on 11/26/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessageResponse {
public static final String INSUFFICIENT_FUNDS = "Insufficient funds";
public static final String ORDER_NOT_FOUND = "order not found";
private String message;

    public ErrorMessageResponse() {
    }

    public ErrorMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
