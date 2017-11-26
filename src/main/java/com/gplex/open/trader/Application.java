package com.gplex.open.trader;

import com.gplex.open.trader.engine.Level2Engine;
import com.gplex.open.trader.service.AccountsServiceImpl;
import com.gplex.open.trader.strategy.SimpleLevel2Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.gplex.open.trader.constant.Const.Products.LTC_USD;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private AccountsServiceImpl accountsService;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    /*@Autowired
    SimpleStrategy simpleStrategy;*/
    @Autowired
    SimpleLevel2Strategy simpleStrategy;



    @Override
    public void run(String... args) throws Exception {

        logger.debug("Starting engine");
        final int[] count = {0};
        new Level2Engine(simpleStrategy, LTC_USD);


        //do something

    }

}