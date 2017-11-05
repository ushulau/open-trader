package com.gplex.open.trader.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gplex.open.trader.domain.ws.TickerMessage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Vlad S. on 11/4/17.
 */
public class SerializationTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(SerializationTest.class);
    private static final String TICKER_PAYLOAD = "{\n" +
            "    \"type\": \"ticker\",\n" +
            "    \"trade_id\": 20153558,\n" +
            "    \"sequence\": 3262786978,\n" +
            "    \"time\": \"2017-09-02T17:05:49.250000Z\",\n" +
            "    \"product_id\": \"BTC-USD\",\n" +
            "    \"price\": \"4388.01000000\",\n" +
            "    \"side\": \"buy\",\n" +
            "    \"last_size\": \"0.03000000\",\n" +
            "    \"best_bid\": \"4388\",\n" +
            "    \"best_ask\": \"4388.01\"\n" +
            "}";
    @Test
    public void testTickerSerrialization() throws IOException {

        TickerMessage  tm = MAPPER.readValue(TICKER_PAYLOAD, TickerMessage.class);
        assertNotNull(tm);

        logger.debug("{}", tm);

        logger.debug("{}", MAPPER.writeValueAsString(tm));

    }


    @Test
    public void testJodaDateFormat() throws ParseException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm.ssSSSS'Z'");
        LocalDateTime now = LocalDateTime.now();
        logger.debug(now.format(df));
    }


}
