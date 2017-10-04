package com.gplex.open.trader.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gplex.open.trader.domain.Order;
import com.gplex.open.trader.domain.TimeResponse;
import com.gplex.open.trader.rest.BaseSecureClient;
import com.gplex.open.trader.utils.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Vlad S. on 9/14/17.
 */
public class OrderServiceImpl extends BaseSecureClient{
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    //private RestTemplate restTemplate;
    //private Security sec;

    private final String baseUrl;


    public OrderServiceImpl(RestTemplate restTemplate, Security sec, String key, String passphrase, String baseUrl) {
        super(restTemplate, sec, key, passphrase);
        this.baseUrl = baseUrl;
    }

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

    public TimeResponse getTime(){
        MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<String, String>();
        String requestPath = this.baseUrl +"/time";
        TimeResponse tr = new TimeResponse();
        tr.setEpoch(new Date().getTime()/1000.0);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "open-trader");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameterMap, headers);
            ResponseEntity<TimeResponse> result = restTemplate.exchange(requestPath, HttpMethod.GET, request, TimeResponse.class);
            tr = result.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        } catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return tr;
    }

    public void getAccountInfo(){
        MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<String, String>();
        String requestPath = this.baseUrl +"/accounts";
        try {
            ResponseEntity<ArrayList> result = executeGET(requestPath, ArrayList.class);
            logger.debug("response {}", result);

        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        }
        catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }



        //headers.add(XboConstants.X1ClientId, XboConstants.TITANIUM_CLIENTID_XBO);
    }


  //  var what = timestamp + method + requestPath + body;
}
