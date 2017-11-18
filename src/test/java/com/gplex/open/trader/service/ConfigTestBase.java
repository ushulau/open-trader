package com.gplex.open.trader.service;

import com.gplex.open.trader.config.RootConfig;
import com.gplex.open.trader.utils.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Vlad S. on 10/8/17.
 */
@SpringBootConfiguration
public class ConfigTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RootConfig.class);

    @Value("${gdax.secret}")
    private String privateKey;
    @Value("${gdax.key}")
    private String key;
    @Value("${gdax.passphrase}")
    private String passphrase;
    @Value("${gdax.api.baseUrl}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public Security security() {
        return new Security(privateKey);
    }

    @Bean
    public AccountsServiceImpl accountsService(RestTemplate rt, Security sec) {
        return new AccountsServiceImpl(rt, sec, key, passphrase, baseUrl);
    }


    @Bean
    public OrderServiceImpl orderService(RestTemplate rt, Security sec) {
        return new OrderServiceImpl(rt, sec, key, passphrase, baseUrl);
    }

    @Bean
    public FillsServiceImpl fillsService(RestTemplate rt, Security sec) {
        return new FillsServiceImpl(rt, sec, key, passphrase, baseUrl);
    }
}
