package com.gplex.open.trader.domain.ws;

import com.gplex.open.trader.constant.Const;

import java.util.Arrays;

/**
 * Created by Vlad S. on 11/4/17.
 */
public class Level2Channel extends  Channel{

    public Level2Channel(String ...productIds) {
        super();
        setName(Const.Channels.LEVEL2);
        setProductIds(Arrays.asList(productIds));
    }

}
