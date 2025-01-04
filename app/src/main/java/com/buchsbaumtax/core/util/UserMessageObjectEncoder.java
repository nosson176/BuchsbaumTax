package com.buchsbaumtax.core.util;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.buchsbaumtax.core.model.UserMessage;
import com.buchsbaumtax.core.model.UserMessageWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserMessageObjectEncoder implements Encoder.Text<UserMessageWebSocket> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(UserMessageWebSocket message) {
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
