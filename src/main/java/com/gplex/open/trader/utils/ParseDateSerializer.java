package com.gplex.open.trader.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Vlad S. on 11/4/17.
 */
public class ParseDateSerializer extends StdSerializer<LocalDateTime> {

    public ParseDateSerializer(){
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(localDateTime.format(ParseDateDeserializer.DATE_TIME_FORMATTER));
    }
}
