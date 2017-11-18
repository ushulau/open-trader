package com.gplex.open.trader;

import com.gplex.open.trader.engine.Engine;
import com.gplex.open.trader.service.AccountsServiceImpl;
import com.gplex.open.trader.strategy.SimpleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.gplex.open.trader.constant.Const.Intervals._15_S;
import static com.gplex.open.trader.constant.Const.Intervals._1_M;
import static com.gplex.open.trader.constant.Const.Products.LTC_USD;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private AccountsServiceImpl accountsService;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    @Autowired
    SimpleStrategy simpleStrategy;



    @Override
    public void run(String... args) throws Exception {

        logger.debug("Starting engine");
        final int[] count = {0};
        /*Engine enginge = new Engine((engine, tickerMessage) -> {
            if(count[0]%10 == 0){
                logger.debug("{}", accountsService.listAccounts());
            }
            count[0]++;
            logger.debug("{}", count[0]);

        }, LTC_USD, _15_S, _1_M);
*/
        new Engine(simpleStrategy,  LTC_USD, _15_S, _1_M,  _1_M);



        //do something

    }

}