package com.gplex.open.trader.service;

import com.gplex.open.trader.domain.Order;
import com.gplex.open.trader.domain.OrderResponse;
import com.gplex.open.trader.rest.BaseSecureClient;
import com.gplex.open.trader.utils.Security;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.gplex.open.trader.constant.Const.Side.BUY;
import static com.gplex.open.trader.constant.Const.Side.SELL;
import static java.util.stream.Collectors.toList;

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

    /**
     * List your current open orders. Only open or un-settled orders are returned. As soon as an order is no longer open and settled, it will no longer appear in the default request.
     *
     * @return
     */
    public List<OrderResponse> listOrders() {
        return listOrders(null, null);
    }


    /**
     * List your current open orders. Only open or un-settled orders are returned. As soon as an order is no longer open and settled, it will no longer appear in the default request.
     *
     * @return
     */
    public List<OrderResponse> listOrders(Set<String> orderIds) {
        List<OrderResponse> orders = listOrders(null, null);
        return orders.stream().filter(order -> orderIds.contains(order.getId())).collect(toList());
    }


    public List<OrderResponse> listOrdersByStatus(String ...statuses) {
        return listOrders(null, statuses);
    }


    public List<OrderResponse> listOrders(String[] products, String[] statuses) {
        String requestPath = this.baseUrl + "/orders";
        if(products == null){
            products = new String[0];
        }
        if(statuses == null){
            statuses = new String[0];
        }
        try {
            URIBuilder builder = new URIBuilder(requestPath);
            for(String p : products){
                builder.addParameter("product_id", p);
            }
            for(String s : statuses){
                builder.addParameter("status", s);
            }
            requestPath = builder.build().toString();
        } catch (Exception e) {
            logger.error("",e);
        }

        try {
            ResponseEntity<List<OrderResponse>> result = executeGET(requestPath, new ParameterizedTypeReference<List<OrderResponse>>(){});
            return result.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        } catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return new ArrayList<>();
    }


    public boolean cancelOrder(OrderResponse order) {
        String requestPath = this.baseUrl + "/orders";
        if(order == null || StringUtils.isBlank(order.getId())){
            return false;
        }
        requestPath += "/" + order.getId();
        logger.debug("canceling order ... [{}]", order.getId());
        try {
            ResponseEntity<String> result = executeDELETE(requestPath, String.class);
            logger.debug("{}", result.getBody());
            return result.getStatusCode().is2xxSuccessful(); //return result.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        } catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return false;
        //return new ArrayList<>();
    }

    public void getOrder(OrderResponse order) {
        String requestPath = this.baseUrl + "/orders";
        if(order == null || StringUtils.isBlank(order.getId())){
            return;
        }
        requestPath += "/" + order.getId();
        logger.debug("fetching order ... [{}]", order.getId());
        try {
            ResponseEntity<String> result = executeGET(requestPath, String.class);
            logger.debug("{}", result.getBody());
            //return result.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        } catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        //return new ArrayList<>();
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
