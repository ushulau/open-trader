package com.gplex.open.trader.utils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Vlad S. on 11/9/17.
 */
public class IntervalChecker {
    private AtomicLong lastSuccess = new AtomicLong(0L);
    private AtomicLong lastCheck = new AtomicLong(0L);
    private long interval;
    public IntervalChecker() {
        this.interval = 5000L;
    }

    public IntervalChecker(long p) {
            this.interval = p;
    }


    public boolean check(){
        long now = new Date().getTime();
        lastCheck.set(now);
        if(lastSuccess.get() + this.interval <= now){
             lastSuccess.set(now);
             return true;
         }
         return false;
    }
}
