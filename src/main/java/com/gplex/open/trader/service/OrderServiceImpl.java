package com.gplex.open.trader.service;

import com.gplex.open.trader.domain.Order;
import com.gplex.open.trader.domain.OrderResponse;
import com.gplex.open.trader.rest.BaseSecureClient;
import com.gplex.open.trader.utils.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.gplex.open.trader.constant.Const.Side.BUY;
import static com.gplex.open.trader.constant.Const.Side.SELL;

/**
 * Created by Vlad S. on 9/14/17.
 */
public class OrderServiceImpl extends BaseSecureClient {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final String baseUrl;


    public OrderServiceImpl(RestTemplate restTemplate, Security sec, String key, String passphrase, String baseUrl) {
        super(restTemplate, sec, key, passphrase);
        this.baseUrl = baseUrl;
    }

    public OrderResponse buyOrder(String product, Double price, Double size) {
        Order order = new Order(product, BUY, price, size);
        return executeOrder(order);
    }

    public OrderResponse sellOrder(String product, Double price, Double size) {
        Order order = new Order(product, SELL, price, size);
        return executeOrder(order);
    }

    private OrderResponse executeOrder(Order order) {
        String requestPath = this.baseUrl + "/orders";
        try {
            ResponseEntity<OrderResponse> result = executePOST(requestPath, order, OrderResponse.class);
            logger.info("{}", result.getBody());
            return result.getBody();
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }


}
