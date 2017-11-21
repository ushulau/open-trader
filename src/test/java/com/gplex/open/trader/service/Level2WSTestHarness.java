package com.gplex.open.trader.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gplex.open.trader.constant.Const;
import com.gplex.open.trader.domain.OrderBook;
import com.gplex.open.trader.domain.Pressure;
import com.gplex.open.trader.domain.ws.*;
import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vlad S. on 9/15/17.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:gdax-keys.properties")
public class Level2WSTestHarness {
    private static final Logger logger = LoggerFactory.getLogger(Level2WSTestHarness.class);
    RestTemplate rt = new RestTemplate();
    @Value("${gdax.secret}")
    private String privateKey;
    @Value("${gdax.key}")
    private String key;
    @Value("${gdax.passphrase}")
    private String passphrase;
    @Value("${gdax.api.baseUrl}")
    private String baseUrl;
    private static final CountDownLatch latch = new CountDownLatch(5);
    private Security sec;
    private AccountsServiceImpl os;
    private double buyPressure = 0.0;
    private double sellPressure = 0.0;
    public static Mac SHARED_MAC;
    private OrderBook orderBook = null;

    static {
        try {
            SHARED_MAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException nsaEx) {
            nsaEx.printStackTrace();
        }
    }

    @PostConstruct
    private void postConstruct() {
        sec = new Security(privateKey);
        os = new AccountsServiceImpl(rt, sec, key, passphrase, baseUrl);
    }


    @Test
    public void testWSClient() throws InterruptedException, URISyntaxException {

        final String WS_URI = "wss://ws-feed.gdax.com:443";
        final Level2ChannelClient client = new Level2ChannelClient(new URI(WS_URI), new Draft_6455());
        final Level2ChannelClient client2 = new Level2ChannelClient(new URI(WS_URI), new Draft_6455());
        //final WebSocketConnectionManager manager = new WebSocketConnectionManager(new WebSocketImpl(), handler, WS_URI);
        //manager.setAutoStartup(true);
        client.setConnectionLostTimeout(20);
        Thread t = new Thread(client);
        Thread t2 = new Thread(client2);
        t.start();
        //t2.start();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        // do stuff

        exec.scheduleAtFixedRate(() -> {
/*

            int mid = orderBook.getCurrentMiddlePoint();
            List<OrderBookRecord> lst = orderBook.getList();
            for(int i = mid-1; i>=0 && i>= mid - 10; i--){
                logger.debug("{}",lst.get(i));
            }
            logger.debug("mid --> {}",lst.get(mid));
            for(int i = mid+1; i< orderBook.getList().size() && i<= mid + 10; i++){
                logger.debug("{}",lst.get(i));
            }

*/


            Pressure pressure = orderBook.getPressure();
            List<Pair<Double, Double>> force = pressure.getForce();
            logger.debug("--------------");
            for(int i = 0; i< 20; i ++){
                logger.debug("{}", force.get(i));

            }





        }, new Date().getTime() % 60000, 30000, TimeUnit.MILLISECONDS);
        /*
        new Thread(() -> {

            logger.debug("" + manager.isRunning());
            try {
                //latch.await();
            } catch (Exception e) {
                logger.error("{}", e);
            }
            this
        }).run();*/



        // });
        //t2.ter
        latch.await();
//        logger.debug("numeber of records in accumulator {}", ac_15s.getMap().size());
    }


    public class Level2ChannelClient extends WebSocketClient {

        Logger logger = LoggerFactory.getLogger(Level2ChannelClient.class);

        public Level2ChannelClient(URI serverUri, Draft protocolDraft) {
            super(serverUri, protocolDraft);
        }

        @Override
        public void onMessage(String message) {
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
                } catch (Exception e) {
                    logger.error("{}", e);
                }
                Pressure pressure = orderBook.getPressure(20);
                double buy = new Double(String.format("%.2f",pressure.getBuy()));
                double sell = new Double(String.format("%.2f",pressure.getSell()));
                if(buy != buyPressure || sell != sellPressure ) {
                  //  logger.debug("buy [{}]  --- sell [{}]  : {}", buy, sell, pressure.getDiff());
                    buyPressure = buy;
                    sellPressure = sell;
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
            channels.add(new Level2Channel(Const.Products.LTC_USD));
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