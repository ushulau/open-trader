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
    private final Map<Long, Candle> map = new HashMap<>();
    private Long lastInterval;


    public Accumulator(Long period) {
        this.period = period;
    }

    public synchronized void add(TickerMessage tm) {
         long l = tm.getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
         if(lastInterval == null){
            lastInterval  = l;
        }

         long index = l / period;
         long startPeriod = index * period;

         if(!map.containsKey(index)){
             map.put(index, new Candle(startPeriod, tm));
             if(map.containsKey(index -1)){
                 logger.info("\n{} -> {}", Utils.intervalString(period), map.get(index -1));
             }
         }else{
             Candle candle = map.get(index);
             candle.add(tm);
         }
     }

    public Map<Long, Candle> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
