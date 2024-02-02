package com.sifradigital.framework.json;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

public final class UtcDateTypeAdapter extends TypeAdapter<Date> {

    private final static TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    @Override
    public void write(JsonWriter out, Date date) throws IOException {
        if (date == null) {
            out.nullValue();
        }
        else {
            String value = DatetimeConverter.format(date, true, UTC_TIME_ZONE);
            out.value(value);
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String date = in.nextString();
            return DatetimeConverter.parse(date);
        }
        catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
