package com.gplex.open.trader.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Date;

/**
 * Created by Vlad S. on 9/15/17.
 */
public class Utils {
    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static String getTs(){
        return  String.format("%.3f",new Date().getTime()/1000.0);
    }


    public static double round(double value){
        return  Double.valueOf(String.format("%.2f",value));
    }

    public static String intervalString(Long elapsedTime){
        Duration duration = Duration.ofMillis(elapsedTime);
       return duration.toString()
               .substring(2)
               .replaceAll("(\\d[HMS])(?!$)", "$1 ")
               .toLowerCase();
    }

}
