package com.gplex.open.trader;

import com.gplex.open.trader.constant.Intervals;
import com.gplex.open.trader.constant.Products;
import com.gplex.open.trader.engine.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.debug("Done");
        final int[] count = {0};
        Engine enginge = new Engine((engine, tickerMessage) -> {

            count[0]++;
            logger.debug("{}", count[0]);



        }, Products.LTC_USD, Intervals._15_S, Intervals._1_M);
    }
}