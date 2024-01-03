package com.sifradigital.framework.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.TimeZone;

public class SqlDateSerializer implements JsonSerializer<Date> {

    private final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        if (date == null) {
            return JsonNull.INSTANCE;
        }
        String value = DatetimeConverter.format(date, false, true, UTC_TIME_ZONE);
        return new JsonPrimitive(value);
    }
}
