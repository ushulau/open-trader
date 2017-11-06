package com.gplex.open.trader.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Vlad S. on 11/4/17.
 */
public class ParseDateDeserializer extends StdDeserializer<LocalDateTime> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER_SHORT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");


    public ParseDateDeserializer(){
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try{
            return LocalDateTime.parse(jsonParser.getValueAsString(), DATE_TIME_FORMATTER);
        }catch (Exception e){
            return LocalDateTime.parse(jsonParser.getValueAsString(), DATE_TIME_FORMATTER_SHORT);
        }
    }
}
