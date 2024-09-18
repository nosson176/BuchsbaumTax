package com.buchsbaumtax.core.util;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.core.model.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.lang.reflect.Type;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonArgumentFactory implements ArgumentFactory {
    private final ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

    public JsonArgumentFactory() {
//        logger.info("JsonArgumentFactory initialized.");

        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // Register support for Java 8 date/time types
                .registerModule(new Jdk8Module())     // Register support for Java 8 optional types
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Write dates as ISO-8601 strings
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))       // Date format for serialization
                .setSerializationInclusion(JsonInclude.Include.ALWAYS)  // Always include null values
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

//    @Override
//    public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
//        logger.info("JsonArgumentFactory - value: {}", value);
//        return Optional.of((position, statement, ctx) -> {
//            try {
//                if (value == null) {
//                    statement.setNull(position, Types.OTHER);
//                } else {
//                    String json = objectMapper.writeValueAsString(value);  // Convert object to JSON
//                    statement.setObject(position, json, Types.OTHER);
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException("Failed to convert object to JSON", e);
//            }
//        });
//    }
@Override
public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
//    logger.info("JsonArgumentFactory - type: {}, value: {}", type, value);

    return Optional.of((position, statement, ctx) -> {
        try {
            if (value == null) {
                statement.setNull(position, Types.OTHER);
            } else if (value instanceof Status) {
                // Only serialize Status objects to JSON
                String json = objectMapper.writeValueAsString(value);
                statement.setObject(position, json, Types.OTHER);
            } else {
                // For all other types, pass the value through without modification
                statement.setObject(position, value);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    });
}
}
