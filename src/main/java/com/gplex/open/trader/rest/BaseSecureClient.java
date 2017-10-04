package com.gplex.open.trader.rest;

import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Vlad S. on 10/1/17.
 */
public class BaseSecureClient {
    private static final Logger logger = LoggerFactory.getLogger(BaseSecureClient.class);
    private static final String OPEN_TRADER = "open-trader";
    private static final String APPLICATION_JSON = "application/json";
    protected final String key;// = "765ee5be8520e3d7718b544b44d761d1";
    protected final String passphrase;// = "test";
    protected final RestTemplate restTemplate;// = "765ee5be8520e3d7718b544b44d761d1";
    protected final Security sec;// = "test";

    //protected final String baseUrl;// = "https://api-public.sandbox.gdax.com";

    public BaseSecureClient(RestTemplate restTemplate, Security sec, String key, String passphrase) {
        this.key = key;
        this.passphrase = passphrase;
        this.restTemplate = restTemplate;
        this.sec = sec;

    }


    public <T> ResponseEntity<T> executeGET(String requestPath, Class<T> responseType) {

        try {
            URI uri = new URI(requestPath);

            String path = uri.getPath();
            HttpHeaders headers = new HttpHeaders();
            String ts = Utils.getTs();
            headers.add("accept", APPLICATION_JSON);
            headers.add("content-type", APPLICATION_JSON);
            headers.add("User-Agent", OPEN_TRADER);
            headers.add("CB-ACCESS-KEY", this.key);
            headers.add("CB-ACCESS-SIGN", sec.signGET(ts, path));
            headers.add("CB-ACCESS-TIMESTAMP", ts);
            headers.add("CB-ACCESS-PASSPHRASE", this.passphrase);
            MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<String, String>();

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameterMap, headers);
            ResponseEntity<T> result = restTemplate.exchange(requestPath, HttpMethod.GET, request, responseType);
            return result;

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
