package com.gplex.open.trader.constant;

/**
 * Created by Vlad S. on 11/5/17.
 */
public class Const {

    public static class OrderType {
        public static final String LIMIT = "limit";
        public static final String MARKET = "market";
        public static final String STOP = "stop";

    }

    public static class Side {
        public static final String BUY = "buy";
        public static final String SELL = "sell";

    }

    /**
     * Created by Vlad S. on 11/4/17.
     */
    public static class Channels {
        public static final String HEARTBEAT = "heartbeat";
        public static final String TICKER = "ticker";
    }

    /**
     * Created by Vlad S. on 11/5/17.
     */
    public static class Intervals {
        public static final Long _15_S = 15000L;
        public static final Long _1_M = 60000L;
        public static final Long _5_M = 5L * _1_M;
        public static final Long _15_M = 15L * _1_M;
        public static final Long _1_H = 60L * _1_M;

    }

    /**
     * Created by Vlad S. on 11/4/17.
     */
    public static class Products {
        public static final String ETH_EUR = "ETH-EUR";
        public static final String LTC_USD = "LTC-USD";
        public static final String BTC_USD = "BTC-USD";
    }
}
