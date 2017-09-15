package com.gplex.open.trader.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gplex.open.trader.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Vlad S. on 9/14/17.
 */
public class OrderServiceImpl {
 private static final ObjectMapper MAPPER = new ObjectMapper();
 private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

 private void buyOrder(String product, Double price, Double size){

     Order order = new Order(product, "buy", size, price);
     String orderString = null;
     try {
         orderString = MAPPER.writeValueAsString(order);
     }catch (Exception e){
         logger.error("",e);
     }
     //orderString.
 }


    private void buyOrder(Double price, Double size ){
       buyOrder("BTC-USD", price, size);

    }


  //  var what = timestamp + method + requestPath + body;
}
