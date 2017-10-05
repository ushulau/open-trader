package com.gplex.open.trader.rest;

import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
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
    protected final String key;
    protected final String passphrase;
    protected final RestTemplate restTemplate;
    protected final Security sec;

    public BaseSecureClient(RestTemplate restTemplate, Security sec, String key, String passphrase) {
        this.key = key;
        this.passphrase = passphrase;
        this.restTemplate = restTemplate;
        this.sec = sec;
    }


    private HttpHeaders createGETHeaders(String timestamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", APPLICATION_JSON);
        headers.add("content-type", APPLICATION_JSON);
        headers.add("User-Agent", OPEN_TRADER);
        headers.add("CB-ACCESS-KEY", this.key);
        headers.add("CB-ACCESS-PASSPHRASE", this.passphrase);
        headers.add("CB-ACCESS-TIMESTAMP", timestamp);
        return headers;
    }


    private HttpEntity<MultiValueMap<String, String>> createGETRequest(String uri) throws URISyntaxException {
        return createGETRequest(uri, null);
    }

    private HttpEntity<MultiValueMap<String, String>> createGETRequest(String url, MultiValueMap<String, String> parameterMap) throws URISyntaxException {
        URI uri = new URI(url);
        String path = uri.getPath();
        if (parameterMap == null) {
            parameterMap = new LinkedMultiValueMap<>();
        }
        String ts = Utils.getTs();
        HttpHeaders headers = createGETHeaders(ts);
        headers.add("CB-ACCESS-SIGN", sec.signGET(ts, path));
        return new HttpEntity<>(parameterMap, headers);
    }


    public <T> ResponseEntity<T> executeGET(String requestPath, Class<T> responseType) {
        try {
            return restTemplate.exchange(requestPath, HttpMethod.GET, createGETRequest(requestPath), responseType);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> ResponseEntity<T> executeGET(String requestPath,  ParameterizedTypeReference<T> responseType) {
        try {
                 return restTemplate.exchange(requestPath, HttpMethod.GET, createGETRequest(requestPath), responseType);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }



    public <T> ResponseEntity<T> executeNotSignedGET(String requestPath, Class<T> responseType) {
        return executeNotSignedGET(requestPath, null, responseType);
    }

    public <T> ResponseEntity<T> executeNotSignedGET(String requestPath, MultiValueMap<String, String> parameterMap, Class<T> responseType) {
        if (parameterMap == null) {
            parameterMap = new LinkedMultiValueMap<String, String>();
        }
        try {
            URI uri = new URI(requestPath);
            String path = uri.getPath();
            HttpHeaders headers = createGETHeaders(path);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameterMap, headers);
            return restTemplate.exchange(requestPath, HttpMethod.GET, request, responseType);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
