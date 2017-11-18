package com.gplex.open.trader.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gplex.open.trader.constant.Const;
import com.gplex.open.trader.domain.Accumulator;
import com.gplex.open.trader.domain.OrderBook;
import com.gplex.open.trader.domain.ws.*;
import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    public static Mac SHARED_MAC;
    public static Accumulator ac_15s = new Accumulator(15000L);
    public static Accumulator ac_1m = new Accumulator(60000L);
    public static Accumulator ac_5m = new Accumulator(5*60000L);
    public static Accumulator ac_15m = new Accumulator(15*60000L);
    public static Accumulator ac_1h = new Accumulator(60*60000L);
    public static List<Accumulator> accumulators = new ArrayList<>();
    private static final OrderBook orderBook = new OrderBook();
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

        /*new Thread(() -> {
             manager.start();
            logger.debug("" + manager.isRunning());
            try {
                //latch.await();
            } catch (Exception e) {
                logger.error("{}", e);
            }
        }).run();

*/


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


      /*  @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            SubscriptionRequest sr = new SubscriptionRequest();
            List<Channel> channels = new ArrayList<>();
            channels.add(new Level2Channel(Const.Products.LTC_USD));
            sr.setChannels(channels);
            String payload = Utils.MAPPER.writeValueAsString(sr);
            logger.debug("--> {}", payload);
            TextMessage message = new TextMessage(payload);
            try {
                session.sendMessage(message);
            }catch (Exception e){
                logger.error("error sending message {}", e);
            }
        }*/

/*
        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            //logger.debug(message.getPayload());
            try {
                TickerMessage tm = Utils.MAPPER.readValue(message.getPayload(), TickerMessage.class);
                for (Accumulator ac : accumulators) {
                    ac.add(tm);
                }
            }catch (Exception e){
                logger.error("can not parse -> {}", message.getPayload());
            }
        }*/



        @Override
        public void onMessage( String message ) {
            if(orderBook == null) {
                try {
                    Level2SnapshotResponse snapshot = Utils.MAPPER.readValue(message, Level2SnapshotResponse.class);
                    initSnapshot(snapshot);
                    return;
                } catch (Exception e) {
                    logger.error("{}", e);
                }
            }else{
                try {
                    Level2UpdateResponse updateMessage = Utils.MAPPER.readValue(message, Level2UpdateResponse.class);
                    orderBook.update(updateMessage);
                    return;
                } catch (Exception e) {
                    logger.error("{}", e);
                }
            }



            send( message );
        }

        @Override
        public void onMessage( ByteBuffer blob ) {
            getConnection().send( blob );
        }

        @Override
        public void onError( Exception ex ) {
            System.out.println( "Error: " );
            ex.printStackTrace();
        }

        @Override
        public void onOpen( ServerHandshake handshake ) {
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
        public void onClose( int code, String reason, boolean remote ) {
            System.out.println( "Closed: " + code + " " + reason );
        }

        @Override
        public void onWebsocketMessageFragment(WebSocket conn, Framedata frame ) {
            FramedataImpl1 builder = (FramedataImpl1) frame;
            builder.setTransferemasked( true );
            getConnection().sendFrame( frame );
        }



    }

    private void initSnapshot(Level2SnapshotResponse snapshot) {

    }

}