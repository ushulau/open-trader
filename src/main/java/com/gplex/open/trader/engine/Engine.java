package com.gplex.open.trader.engine;

import com.gplex.open.trader.domain.Accumulator;
import com.gplex.open.trader.domain.ws.Channel;
import com.gplex.open.trader.domain.ws.SubscriptionRequest;
import com.gplex.open.trader.domain.ws.TickerChannel;
import com.gplex.open.trader.domain.ws.TickerMessage;
import com.gplex.open.trader.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Vlad S. on 11/5/17.
 */
public class Engine {
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);
    private final String product;
    private List<Long> intervals;
    private final TickSubscriber tickSubscriber;
    private final Engine instance;
    private final String WS_URI = "wss://ws-feed.gdax.com:443";
    private final CountDownLatch latch = new CountDownLatch(1);
    private final Map<String , Accumulator> accumulatorMap = new HashMap<>();
    private final Map<String , Accumulator> unmodifiableMapAccumMap = Collections.unmodifiableMap(accumulatorMap);
    public Engine(TickSubscriber tickSubscriber, String product, Long ... intervals) {
        this.tickSubscriber = tickSubscriber;
        this.product = product;
        this.instance = this;
        WebSocketHandler handler = new SimpleClientWebSocketHandler();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(new StandardWebSocketClient(), handler, WS_URI);
        manager.setAutoStartup(true);

        for(Long interval : intervals){
            Accumulator ac = new Accumulator(interval);
            accumulatorMap.put(ac.getName(), ac);
        }

        new Thread(() -> {
            manager.start();
            logger.debug("" + manager.isRunning());
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.error("{}", e);
            }
        }).run();


    }


    public class SimpleClientWebSocketHandler extends TextWebSocketHandler {
        Logger logger = LoggerFactory.getLogger(SimpleClientWebSocketHandler.class);



        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            SubscriptionRequest sr = new SubscriptionRequest();
            List<Channel> channels = new ArrayList<>();
            channels.add(new TickerChannel(product));
            sr.setChannels(channels);
            String payload = Utils.MAPPER.writeValueAsString(sr);
            logger.debug("--> {}", payload);
            TextMessage message = new TextMessage(payload);
            session.sendMessage(message);
        }

        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            //logger.debug(message.getPayload());
            try {
                TickerMessage tm = Utils.MAPPER.readValue(message.getPayload(), TickerMessage.class);
                for (Accumulator ac : accumulatorMap.values()) {
                    ac.add(tm);
                }
                tickSubscriber.onTick(instance,tm);
            }catch (Exception e){
                logger.error("can not parse -> {}", message.getPayload());
            }
        }

    }


    public Map<String, Accumulator> getAccumulatorMap() {
        return unmodifiableMapAccumMap;
    }
}
