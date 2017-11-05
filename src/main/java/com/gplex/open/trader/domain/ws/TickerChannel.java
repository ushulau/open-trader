package com.gplex.open.trader.domain.ws;

import com.gplex.open.trader.constant.Const;

import java.util.Arrays;

/**
 * Created by Vlad S. on 11/4/17.
 */
public class TickerChannel extends  Channel{

    public TickerChannel( String ...productIds) {
        super();
        setName(Const.Channels.TICKER);
        setProductIds(Arrays.asList(productIds));
    }

}
