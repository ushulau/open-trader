package com.gplex.open.trader;

import com.gplex.open.trader.constant.Const;
import com.gplex.open.trader.engine.Engine;
import com.gplex.open.trader.service.AccountsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private AccountsServiceImpl accountsService;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }



    @Override
    public void run(String... args) throws Exception {

        logger.debug("Starting engine");
        final int[] count = {0};
        Engine enginge = new Engine((engine, tickerMessage) -> {
            if(count[0]%10 == 0){
                logger.debug("{}", accountsService.listAccounts());
            }
            count[0]++;
            logger.debug("{}", count[0]);



        }, Const.Products.LTC_USD, Const.Intervals._15_S, Const.Intervals._1_M);

        //do something

    }

}