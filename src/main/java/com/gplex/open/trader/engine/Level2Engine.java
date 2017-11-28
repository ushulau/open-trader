package com.gplex.open.trader.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gplex.open.trader.domain.OrderBook;
import com.gplex.open.trader.domain.ws.*;
import com.gplex.open.trader.utils.Utils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Vlad S. on 11/5/17.
 */
public class Level2Engine {
    private static final Logger logger = LoggerFactory.getLogger(Level2Engine.class);
    private final Level2Engine instance;
    private final TickSubscriber tickSubscriber;
    private final String WS_URI = "wss://ws-feed.gdax.com:443";
    private Level2ChannelClient client;
    private final String product;
    private double buyPressure = 0.0;
    private double sellPressure = 0.0;
    private OrderBook orderBook = null;
    private AtomicLong counter = new AtomicLong(0);
    private long lastCounter = -1;
    private URI createURI(String ws_uri) {
        URI uri = null;
        try {
            uri = new URI(WS_URI);
        } catch (URISyntaxException e) {
            logger.error("", e);

        }
        return uri;
    }

    private final CountDownLatch latch = new CountDownLatch(1);

    public Level2Engine(TickSubscriber tickSubscriber,String product) {
        this.tickSubscriber = tickSubscriber;
        this.product = product;
        this.instance = this;
        client = new Level2ChannelClient(createURI(WS_URI), new Draft_6455());
        client.setConnectionLostTimeout(20);
        final Thread[] t = {new Thread(client)};
        t[0].start();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        // do stuff

       exec.scheduleAtFixedRate(() -> {
            long c =  counter.longValue();
            if(c == lastCounter){
                logger.warn("\n+------------------------------------------+" +
                            "\n|      SOCKET is STALE RESUBSCRIBING...    |" +
                            "\n+------------------------------------------+");
                //todo need to resubscribe
                client.close();
                t[0].interrupt();
                orderBook = null;
                client = new Level2ChannelClient(createURI(WS_URI), new Draft_6455());
                client.setConnectionLostTimeout(20);
                t[0] = new Thread(client);
                t[0].start();

            }else {
                try {
                    orderBook.getPressure();
                    logger.info(
                            "\n+----------------------------------------+\n" +
                                    "|" + Utils.padSpace("HEARTBEAT: [" + c + "]", 40) + "|\n" +
                                    "|" + Utils.padSpace("Current price -> $" + orderBook.getList().get(orderBook.getCurrentMiddlePoint()).getPrice(), 40) + "|\n" +
                                    "|" + Utils.padSpace("$0.25 range buy pressure -> " + orderBook.getVolume(25) + "%", 40) + "|\n" +
                                    "|" + Utils.padSpace("$0.50 range buy pressure -> " + orderBook.getVolume(50) + "%", 40) + "|\n" +
                                    "|" + Utils.padSpace("$1.00 range buy pressure -> " + orderBook.getVolume(100) + "%", 40) + "|\n" +
                                    "|" + Utils.padSpace("$2.00 range buy pressure -> " + orderBook.getVolume(200) + "%", 40) + "|\n" +
                                    "+----------------------------------------+");
                }catch (Exception e){
                    logger.error("Heartbeat error [{}]", e);
                }

                try {
                    tickSubscriber.onHeartbeat();
                }catch (Exception e){
                    logger.error("ticksubscriber on heartbeat error \n{}", e);
                }
            }

            lastCounter = c;
        }, new Date().getTime() % 60000, 30000, TimeUnit.MILLISECONDS);

    }


    public class Level2ChannelClient extends WebSocketClient implements Runnable {

        Logger logger = LoggerFactory.getLogger(Level2ChannelClient.class);

        public Level2ChannelClient(URI serverUri, Draft protocolDraft){
            super(serverUri, protocolDraft);
        }

        @Override
        public void onMessage(String message) {
            counter.incrementAndGet();
            if (orderBook == null) {
                try {
                    Level2SnapshotResponse snapshot = Utils.MAPPER.readValue(message, Level2SnapshotResponse.class);
                    initSnapshot(snapshot);
                } catch (Exception e) {
                    logger.error("{}", e);
                }
            } else {
                try {
                    Level2UpdateResponse updateMessage = Utils.MAPPER.readValue(message, Level2UpdateResponse.class);
                    orderBook.add(updateMessage);
                    tickSubscriber.onUpdate(orderBook);
                } catch (Exception e) {
                    logger.error("{}", e);
                }
            }
        }

        @Override
        public void onMessage(ByteBuffer blob) {
            getConnection().send(blob);
        }

        @Override
        public void onError(Exception ex) {
            System.out.println("Error: ");
            ex.printStackTrace();
        }

        @Override
        public void onOpen(ServerHandshake handshake) {
            SubscriptionRequest sr = new SubscriptionRequest();
            List<Channel> channels = new ArrayList<>();
            channels.add(new Level2Channel(product));
            sr.setChannels(channels);
            String payload = null;
            try {
                payload = Utils.MAPPER.writeValueAsString(sr);
            } catch (JsonProcessingException e) {

                logger.error("", e);
            }
            logger.debug("--> {}", payload);

            getConnection().send(payload);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Closed: " + code + " " + reason);
        }

        @Override
        public void onWebsocketMessageFragment(WebSocket conn, Framedata frame) {
            FramedataImpl1 builder = (FramedataImpl1) frame;
            builder.setTransferemasked(true);
            getConnection().sendFrame(frame);
        }


    }


    private void initSnapshot(Level2SnapshotResponse snapshot) {
        orderBook = new OrderBook();
        orderBook.add(snapshot);
    }





}
