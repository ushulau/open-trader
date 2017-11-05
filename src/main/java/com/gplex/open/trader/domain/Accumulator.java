package com.gplex.open.trader.domain;

import com.gplex.open.trader.domain.ws.TickerMessage;
import com.gplex.open.trader.utils.Utils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vlad S. on 11/4/17.
 */
public class Accumulator {
    private static final Logger logger = LoggerFactory.getLogger(Accumulator.class);
    private final Long period;
    private final String name;
    private final Map<Long, Candle> map = new HashMap<>();
    private Long lastInterval;


    public Accumulator(Long period) {
        this.period = period;
        this.name = Utils.intervalString(period);
    }

    public synchronized void add(TickerMessage tm) {
        long l = tm.getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
        if (lastInterval == null) {
            lastInterval = l;
        }

        long index = l / period;
        long startPeriod = index * period;

        if (!map.containsKey(startPeriod)) {
            map.put(startPeriod, new Candle(startPeriod, tm));
            if (map.containsKey(startPeriod - period)) {
                logger.info("\n{} -> {}", Utils.intervalString(period), map.get(startPeriod - period));
            }
        } else {
            Candle candle = map.get(startPeriod);
            candle.add(tm);
        }
    }

    public Map<Long, Candle> getMap() {
        return map;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
