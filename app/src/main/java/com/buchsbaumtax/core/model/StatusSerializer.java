package com.buchsbaumtax.core.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class StatusSerializer extends JsonSerializer<Status> {
    @Override
    public void serialize(Status status, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        // Handle potential null values safely
        if (status.getDate() != null) {
            gen.writeNumberField("date", status.getDate().longValue());
        } else {
            gen.writeNullField("date"); // Optionally, write null for the date field
        }

        if (status.getValue() != null) {
            gen.writeStringField("value", status.getValue());
        } else {
            gen.writeNullField("value"); // Write null explicitly for the value field
        }

        gen.writeEndObject();
    }
}
