package com.gplex.open.trader.domain;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vlad S. on 11/16/17.
 */
public class OrderBookTest {
    private static final Logger logger = LoggerFactory.getLogger(OrderBookRecord.class);
    @Test
    public void testInsert(){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        // Now, we will insert the number
        list.add(4, 87);
        logger.debug("{}", list);
        assertEquals(list.get(4), new Integer(87));
    }


    @Test
    public void testRemove(){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.remove(4);
        logger.debug("{}", list);
        assertEquals(list.get(4), new Integer(6));
    }

    @Test
    public void testBinaryFind(){
        OrderBook ob = getOrderBook();

        logger.debug("{}", ob.list);

        int res = ob.getElementId(1.56);

        logger.debug("-> {} = {}", res, ob.list.get(res));

    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void testOrderBook2(){
        OrderBook ob = getOrderBook();

        logger.debug("{}", ob.list);

        int res = ob.getElementId(5.56);

        logger.debug("-> {} = {}", res, ob.list.get(res));

    }


    @Test
    public void verifyOrderTest(){
        OrderBook ob = getOrderBook();
        OrderBookRecord prev = null;
        for(OrderBookRecord record: ob.list){
            if(prev != null){
                logger.debug("{} > {}", record.getPrice(), prev.getPrice());
                assertTrue(record.getPrice() > prev.getPrice());
            }
            prev = record;

        }
        logger.debug("{}", ob.list);

    }



    @Test
    public void verifyOrderTestWithUpdate(){
        OrderBook ob = getOrderBook();
        OrderBookRecord prev = null;
        ob.add(new OrderBookRecord("update", 1.25, 100.111));
        ob.add(new OrderBookRecord("insert", 1.26, 33.33));
        for(OrderBookRecord record: ob.list){
            if(prev != null){
                logger.debug("{}[{}] > {}[{}]", record.getPrice(), record.getSize(), prev.getPrice(), prev.getSize());
                assertTrue(record.getPrice() > prev.getPrice());
            }
            prev = record;
        }
        logger.debug("{}", ob.list);

    }


    @Test
    public void verifyOrderTestWithUpdateDelete(){
        OrderBook ob = getOrderBook();
        OrderBookRecord prev = null;
        ob.add(new OrderBookRecord("update", 1.25, 100.111));
        ob.add(new OrderBookRecord("insert", 1.26, 33.33));
        ob.add(new OrderBookRecord("delete", 1.8, 0.0));
        ob.add(new OrderBookRecord("delete", 45.8, 0.0));
        ob.add(new OrderBookRecord("delete", 1.05, 0.0));
        for(OrderBookRecord record: ob.list){
            if(prev != null){
                logger.debug("{}[{}] > {}[{}]", record.getPrice(), record.getSize(), prev.getPrice(), prev.getSize());
                assertTrue(record.getPrice() > prev.getPrice());
            }
            prev = record;
        }
        logger.debug("{}", ob.list);

    }


    @Test
    public void testGettingMiddlePoint(){
        OrderBook ob = getOrderBook();

        int mid = ob.getCurrentMiddlePoint();
        logger.debug("current mid point => {} ", ob.list.get(mid));
        for(OrderBookRecord record: ob.list){
            logger.debug("{}", record);

        }

    }

    @Test
    public void testGettingMiddlePointAllSell(){
        OrderBook ob = getSellOnlyOrderBook();

        int mid = ob.getCurrentMiddlePoint();
        logger.debug("current mid point => {} ", ob.list.get(mid));
        for(OrderBookRecord record: ob.list){
            logger.debug("{}", record);

        }

    }



    @Test
    public void testPressure(){
        OrderBook ob = getOrderBook();
        Pressure pressure = ob.getPressure();
        List<Pair<Double, Double>> force = pressure.getForce();

        for(Pair<Double, Double> pair: force){
            logger.debug("{}", pair);

        }

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGettingMiddlePointAllBuy(){
        OrderBook ob = getBuyOnlyOrderBook();

        for(OrderBookRecord record: ob.list){
            logger.debug("{}", record);
        }
        int mid = ob.getCurrentMiddlePoint();
        logger.debug("current mid point => {} ", ob.list.get(mid));
    }


    private OrderBook getOrderBook() {
        OrderBook ob = new OrderBook();
        List<OrderBookRecord> list = new ArrayList<>();
        list.add(new OrderBookRecord("delete", "0.05","0"));
        list.add(new OrderBookRecord("sell", "1.45","13.123"));
        list.add(new OrderBookRecord("buy", "1.05","1"));
        list.add(new OrderBookRecord("buy", "1.1","1"));
        list.add(new OrderBookRecord("sell", "1.8","1"));

        list.add(new OrderBookRecord("buy", "1.2","1"));
        list.add(new OrderBookRecord("buy", "1.25","1"));

        list.add(new OrderBookRecord("buy", "1.15","1"));

        list.add(new OrderBookRecord("sell", "1.50","1"));
        list.add(new OrderBookRecord("sell", "1.55","1"));

        list.add(new OrderBookRecord("sell", "1.95","1"));
        list.add(new OrderBookRecord("sell", "2.45","1.32"));
        list.add(new OrderBookRecord("buy", "1.40","1.32"));
        list.add(new OrderBookRecord("delete", "45.05","0"));
        ob.addAll(list);
        return ob;
    }


    private OrderBook getSellOnlyOrderBook() {
        OrderBook ob = new OrderBook();
        List<OrderBookRecord> list = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            list.add(new OrderBookRecord("sell", String.format("%.2f",  Math.random()* 10.0) ,String.format("%.2f",  Math.random()* 1000.0)));
        }
        ob.addAll(list);
        return ob;
    }


    private OrderBook getBuyOnlyOrderBook() {
        OrderBook ob = new OrderBook();
        List<OrderBookRecord> list = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            list.add(new OrderBookRecord("buy", String.format("%.2f",  Math.random()* 10.0) ,String.format("%.2f",  Math.random()* 1000.0)));
        }
        ob.addAll(list);
        return ob;
    }

}