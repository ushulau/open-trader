package com.gplex.open.trader.domain;

import com.gplex.open.trader.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Created by Vlad S. on 11/27/17.
 */
public class OrderException extends Exception {
    public static final String NOT_DEFINED_EXCEPTION = "NOT DEFINED EXCEPTION";

    private static final Logger logger = LoggerFactory.getLogger(OrderException.class);
    ErrorMessageResponse msg;


    public OrderException(HttpStatusCodeException exeption) {
        super(exeption.getMessage());
        try {
            this.msg = Utils.MAPPER.readValue(exeption.getResponseBodyAsString(), ErrorMessageResponse.class);
        } catch (Exception e) {
            this.msg = new ErrorMessageResponse(NOT_DEFINED_EXCEPTION);
        }
    }

    public OrderException(String msg) {
        this.msg = new ErrorMessageResponse(msg);
    }

    public OrderException() {
        this.msg = new ErrorMessageResponse(NOT_DEFINED_EXCEPTION);
    }

    public String getMessage() {
        if (msg != null) {
            return msg.getMessage();
        }
        return NOT_DEFINED_EXCEPTION;
    }


    public boolean isInsufficientFunds(){
        return is(ErrorMessageResponse.INSUFFICIENT_FUNDS);
    }

    public boolean isOrderNotFound(){
        return is(ErrorMessageResponse.ORDER_NOT_FOUND);
    }

    private boolean is(String message){
        if(message == null || msg == null || msg.getMessage() == null){
            return  false;
        }
        return message.equalsIgnoreCase(msg.getMessage());
    }



}
