package com.buchsbaumtax.core.util;

import com.buchsbaumtax.core.model.AlarmMessageWebSocket;
import com.buchsbaumtax.core.model.UserMessageWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class AlarmMessageObjectEncoder implements Encoder.Text<AlarmMessageWebSocket> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(AlarmMessageWebSocket message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding UserMessageObject", e);
        }
    }

    @Override
    public void init(EndpointConfig config) {
        // Initialization logic if required
    }

    @Override
    public void destroy() {
        // Cleanup if required
    }
}
