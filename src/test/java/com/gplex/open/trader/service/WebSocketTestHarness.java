package com.gplex.open.trader.service;

import com.gplex.open.trader.constant.Products;
import com.gplex.open.trader.domain.Account;
import com.gplex.open.trader.domain.Accumulator;
import com.gplex.open.trader.domain.TimeResponse;
import com.gplex.open.trader.domain.ws.Channel;
import com.gplex.open.trader.domain.ws.SubscriptionRequest;
import com.gplex.open.trader.domain.ws.TickerChannel;
import com.gplex.open.trader.domain.ws.TickerMessage;
import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
import org.eclipse.jetty.client.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.JettyXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;

/**
 * Created by Vlad S. on 9/15/17.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:gdax-keys.properties")
public class WebSocketTestHarness{
    private static final Logger logger = LoggerFactory.getLogger(WebSocketTestHarness.class);
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
    final CountDownLatch disconnectLatch = new CountDownLatch(10);
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
    public void testConnection() {
        List<Account> accounts = os.listAccounts();
        logger.debug("{}", new Object[]{accounts});
    }

    private void addAllAccumulators(){
        accumulators.add(ac_15s);
        accumulators.add(ac_1m);
        accumulators.add(ac_5m);
        accumulators.add(ac_15m);
        accumulators.add(ac_1h);

    }
    @Test
    public void testWSClient() throws InterruptedException {

        final String WS_URI = "wss://ws-feed.gdax.com:443";
        WebSocketHandler handler = new SimpleClientWebSocketHandler();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(new StandardWebSocketClient(), handler, WS_URI);
        manager.setAutoStartup(true);
        manager.start();
        logger.debug(""+manager.isRunning());

        latch.await();
        logger.debug("numeber of records in accumulator {}", ac_15s.getMap().size());
    }


        @Test
    public void testWSSClient() throws InterruptedException {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();

        List<Account> accounts = os.listAccounts();
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        HttpClient jettyHttpClient = new HttpClient();
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(webSocketClient));
        transports.add(new JettyXhrTransport(jettyHttpClient));

        SockJsClient sockJsClient = new SockJsClient(transports);

        String stompUrl = "ws://{host}:{port}";
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter());
        stompClient.setTaskScheduler(taskScheduler);
        stompClient.setDefaultHeartbeat(new long[] {0, 0});
        StopWatch stopWatch = new StopWatch("STOMP Broker Relay WebSocket Load Tests");
        stopWatch.start();
        final CountDownLatch connectLatch = new CountDownLatch(10);
        final CountDownLatch subscribeLatch = new CountDownLatch(10);
        final CountDownLatch messageLatch = new CountDownLatch(10);
        final CountDownLatch disconnectLatch = new CountDownLatch(10);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        ConsumerStompSessionHandler consumer = new ConsumerStompSessionHandler(200, connectLatch,
                subscribeLatch, messageLatch, disconnectLatch, failure);

        stompClient.connect(stompUrl, consumer, "ws-feed.gdax.com", 80);


        if (!subscribeLatch.await(5000, TimeUnit.MILLISECONDS)) {
            fail("Not all users subscribed, remaining: " + subscribeLatch.getCount());
        }
        stopWatch.stop();
        logger.debug("Finished: " + stopWatch.getLastTaskTimeMillis() + " millis");

    }



    @Test
    public void testTs() {
        Double ct = new Date().getTime() / 1000.0;
        TimeResponse ts = os.getTime();
        logger.debug("{} ~ {}", String.format("%.3f", ct), String.format("%.3f", ts.getEpoch()));
    }


    private static class ConsumerStompSessionHandler extends StompSessionHandlerAdapter {

        private final int expectedMessageCount;

        private final CountDownLatch connectLatch;

        private final CountDownLatch subscribeLatch;

        private final CountDownLatch messageLatch;

        private final CountDownLatch disconnectLatch;

        private final AtomicReference<Throwable> failure;

        private AtomicInteger messageCount = new AtomicInteger(0);


        public ConsumerStompSessionHandler(int expectedMessageCount, CountDownLatch connectLatch,
                                           CountDownLatch subscribeLatch, CountDownLatch messageLatch, CountDownLatch disconnectLatch,
                                           AtomicReference<Throwable> failure) {

            this.expectedMessageCount = expectedMessageCount;
            this.connectLatch = connectLatch;
            this.subscribeLatch = subscribeLatch;
            this.messageLatch = messageLatch;
            this.disconnectLatch = disconnectLatch;
            this.failure = failure;
        }

        @Override
        public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
            this.connectLatch.countDown();
            session.setAutoReceipt(true);
            session.subscribe("", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    if (messageCount.incrementAndGet() == expectedMessageCount) {
                        messageLatch.countDown();
                        disconnectLatch.countDown();
                        latch.countDown();
                        session.disconnect();
                    }
                }
            }).addReceiptTask(() -> subscribeLatch.countDown());

        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            logger.error("Transport error", exception);
            this.failure.set(exception);
            if (exception instanceof ConnectionLostException) {
                this.disconnectLatch.countDown();
            }
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            logger.error("Handling exception", ex);
            this.failure.set(ex);
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            Exception ex = new Exception(headers.toString());
            logger.error("STOMP ERROR frame", ex);
            this.failure.set(ex);
        }

        @Override
        public String toString() {
            return "ConsumerStompSessionHandler[messageCount=" + this.messageCount + "]";
        }
    }

    public class SimpleClientWebSocketHandler extends TextWebSocketHandler {

        Logger logger = LoggerFactory.getLogger(SimpleClientWebSocketHandler.class);



        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            SubscriptionRequest sr = new SubscriptionRequest();
            List<Channel> channels = new ArrayList<>();
            channels.add(new TickerChannel(Products.LTC_USD));
            sr.setChannels(channels);
            String payload = Utils.MAPPER.writeValueAsString(sr);
            logger.debug("--> {}", payload);
            TextMessage message = new TextMessage(payload);
            session.sendMessage(message);
        }

        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            logger.debug(message.getPayload());
            try {
                TickerMessage tm = Utils.MAPPER.readValue(message.getPayload(), TickerMessage.class);
                for (Accumulator ac : accumulators) {
                    ac.add(tm);
                }
            }catch (Exception e){
                logger.error("can not parse");
            }
        }

    }

}