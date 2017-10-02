package com.gplex.open.trader.rest;

import com.gplex.open.trader.utils.Security;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Vlad S. on 10/1/17.
 */
public class BaseSecureClient {
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


}
