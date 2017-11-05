package com.gplex.open.trader.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource(value = {"classpath:app.properties", "classpath:gdax-keys.properties"})
public class RootConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RootConfig.class);


    /*@Value("${gdax.auth.endpoint}")
    private String gdaxAuthEndpont;*/

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}


