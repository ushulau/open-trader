package com.gplex.open.trader.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gplex.open.trader.domain.Order;
import com.gplex.open.trader.domain.TimeResponse;
import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
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
public class OrderServiceImpl {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private RestTemplate restTemplate;
    private Security sec;
    private final String key = "765ee5be8520e3d7718b544b44d761d1";
    private final String secret = "jsxvksOKUfwk1nhiC3q7GoGyokCcg52YZ8YH/x6JKLKSYeJJi6IC6wNNztf2wo/fM9XoVei0ClANpLV191oW2w==";
    private final String passphrase = "test";
    private final String baseUrl = "https://api-public.sandbox.gdax.com";

    public OrderServiceImpl(RestTemplate restTemplate, Security sec) {
        this.restTemplate = restTemplate;
        this.sec = sec;
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
            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String ts = Utils.getTs();
            //headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("accept", "application/json");
            headers.add("content-type", "application/json");
            headers.set("User-Agent","open-trader");
            headers.add("CB-ACCESS-KEY",this.key);
            headers.add("CB-ACCESS-SIGN", sec.signGET(ts, "/accounts"));
            headers.add("CB-ACCESS-TIMESTAMP",ts);
            headers.add("CB-ACCESS-PASSPHRASE",this.passphrase);

            //parameterMap.add(PARAMETER_CLIENT_ID, this.clientId);
            //parameterMap.add(PARAMETER_CLIENT_SECRET, this.clientSecret);
            //parameterMap.add(PARAMETER_GRANT_TYPE, GRANT_TYPE_CLIENT_CREDENTIALS);
            /*if(this.oauthScope != null) {
                parameterMap.add(PARAMETER_SCOPE, this.oauthScope);
            }*/
            Long tokenRetrievalStartTime = System.currentTimeMillis();
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameterMap, headers);
            ResponseEntity<ArrayList> result = restTemplate.exchange(requestPath, HttpMethod.GET, request, ArrayList.class);
            //OAuthToken token = result.getBody();
            //this.tokenRetrievalTime = tokenRetrievalStartTime;
            logger.debug("response {}", result);
           // return token;
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
