package com.gplex.open.trader.strategy;

import com.gplex.open.trader.constant.Const;
import com.gplex.open.trader.domain.Accumulator;
import com.gplex.open.trader.domain.Candle;
import com.gplex.open.trader.domain.FillResponse;
import com.gplex.open.trader.domain.OrderResponse;
import com.gplex.open.trader.domain.ws.TickerMessage;
import com.gplex.open.trader.engine.Engine;
import com.gplex.open.trader.engine.TickSubscriber;
import com.gplex.open.trader.service.FillsServiceImpl;
import com.gplex.open.trader.service.OrderServiceImpl;
import com.gplex.open.trader.utils.IntervalChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gplex.open.trader.constant.Const.OrderStatus.*;
import static com.gplex.open.trader.utils.Utils.round;
import static java.util.stream.Collectors.toSet;

/**
 * Created by Vlad S. on 11/7/17.
 */
@Service
public class SimpleStrategy implements TickSubscriber {
    private static final Logger logger = LoggerFactory.getLogger(SimpleStrategy.class);

    @Autowired
    OrderServiceImpl orderService;


    @Autowired
    FillsServiceImpl fillService;


    private Map<String, OrderResponse> openOrders = null;

    private AtomicInteger maxAllowedOrders = new AtomicInteger(1);
    private AtomicInteger tickCounter = new AtomicInteger(0);

    private IntervalChecker fiveSecond = new IntervalChecker();
    private IntervalChecker minute = new IntervalChecker(60000L);


    @Override
    public void onTick(Engine engine, TickerMessage tickerMessage) {
        int tick = tickCounter.incrementAndGet();
        if (openOrders == null) {
            initOpenOrders();
        }

        if (minute.check() && maxAllowedOrders.get() == 0) {
            maxAllowedOrders.set(1);
        }

        Accumulator a15s = engine.getAccumulatorMap().get("15s");
        Accumulator a1m = engine.getAccumulatorMap().get("1m");
        Accumulator a5m = engine.getAccumulatorMap().get("5m");

        Candle c0 = a15s.getCurrent();
        Candle c1 = a15s.getPrevious();
        //Candle c2 = a15s.getPrevious(2L);
        if (fiveSecond.check()) {


            List<OrderResponse> stillOpen = orderService.listOrders(openOrders.keySet());

            Set<String> stillOpenIds = stillOpen.stream().map(order -> order.getId()).collect(toSet());
            Set<String> closedIds = openOrders.keySet().stream().filter(k -> !stillOpenIds.contains(k)).collect(toSet());

            for (OrderResponse order : stillOpen) {
                if ("buy".equalsIgnoreCase(order.getSide())) {

                    if (tickerMessage.getPrice().subtract(order.getPrice()).doubleValue() >= 0.3) {
                        logger.debug("canceling order because of beeing not in range");
                        orderService.cancelOrder(order);
                        openOrders.remove(order.getId());
                    }


                }


            }


            if (closedIds.size() > 0) {
                List<FillResponse> filled = fillService.listFills(closedIds);
                for (FillResponse fill : filled) {
                    openOrders.remove(fill.getOrderId());
                    logger.debug("{} settled -> {}\n{}", fill.getOrderId(), fill.getSettled(), fill);

                    //{"id":"9290a9b1-dbb2-4c64-b369-c8c439b8e055","price":60.46000000,"size":0.10000000,"productId":"LTC-USD","side":"buy","stp":"dc","type":"limit","timeInForce":"GTC","postOnly":true,"createdAt":"2017-11-11T02:33:23.190888","fillFees":0E-16,"filledSize":0E-8,"executedValue":0E-16,"status":"open","settled":false}
                    //createAcounter offer for sell

                    if ("buy".equalsIgnoreCase(fill.getSide())) {
                        OrderResponse sellOrder = orderService.sellOrder(fill.getProductId(), round(Math.max(fill.getPrice().doubleValue(), tickerMessage.getPrice().doubleValue()) + 0.02), fill.getSize().doubleValue());
                        logger.debug("{} reverse sell order created");
                    }

                }
            }

            List<OrderResponse> result = orderService.listOrdersByStatus(ACTIVE, PENDING, OPEN);

            for (OrderResponse or : result) {
                if (or.getSettled()) {
                    logger.debug("settled -> {}", or);
                }
            }
//            logger.debug("{}", result);


        }

        if (c0 != null) {
            if (/*c1.getOpen() < c1.getClose() &&*/ c0.getOpen() < c0.getClose() /*&& c1.getClose() <= c0.getOpen()*/) {
                if (maxAllowedOrders.get() >= 1) {
                    OrderResponse order = null;
                    OrderResponse order1 = null;
                    OrderResponse order2 = null;
                    try {
                        order = orderService.buyOrder(Const.Products.LTC_USD, round(tickerMessage.getPrice().doubleValue() - 0.01), 0.01);

                    } finally {
                        if (order != null) {
                            logger.debug("opened order {}", order);
                            openOrders.put(order.getId(), order);

                        }
                        maxAllowedOrders.decrementAndGet();
                    }
                    logger.debug("GOOD TIME TO BUY");
                }

            }


        } else if (tick % 10 == 0 && a15s.getMap().size() <= 2) {

            logger.debug("waiting to warm up the engine ...");
        }

           /* if (tick % 10 == 0) {
                logger.debug("[{}] open orders \n{}", openOrders.size(), openOrders);
            }*/


    }

    private void initOpenOrders() {
        openOrders = new ConcurrentHashMap<>();
        List<OrderResponse> result = orderService.listOrders(new String[]{Const.Products.LTC_USD}, new String[]{Const.OrderStatus.OPEN});
        if (result != null) {
            for (OrderResponse or : result) {
                if ("buy".equalsIgnoreCase(or.getSide())) {
                    openOrders.put(or.getId(), or);
                }
            }
        }
    }
}
