package com.gplex.open.trader.engine;

import com.gplex.open.trader.domain.ws.TickerMessage;

/**
 * Created by Vlad S. on 11/5/17.
 */
public interface TickSubscriber {

    void onTick(Engine engine, TickerMessage tickerMessage);

}
