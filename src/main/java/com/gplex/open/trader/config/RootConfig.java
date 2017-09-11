package com.gplex.open.trader.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Vlad S. on 4/23/17.
 */
@Configuration
@PropertySource(value = {"classpath:app.properties"})
public class RootConfig {
}
