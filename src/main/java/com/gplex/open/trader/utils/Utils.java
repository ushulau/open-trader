package com.gplex.open.trader.utils;

import java.util.Date;

/**
 * Created by Vlad S. on 9/15/17.
 */
public class Utils {

    public static String getTs(){
        return  String.format("%.3f",new Date().getTime()/1000.0);
    }
}
