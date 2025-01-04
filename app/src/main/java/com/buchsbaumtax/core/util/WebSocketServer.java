package com.buchsbaumtax.core.util;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.buchsbaumtax.app.dto.UserMessageObject;
import com.buchsbaumtax.core.model.AlarmMessageWebSocket;
import com.buchsbaumtax.core.model.UserMessage;
import com.buchsbaumtax.core.model.UserMessageWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value = "/websocket/{userId}", encoders = {UserMessageObjectEncoder.class, AlarmMessageObjectEncoder.class})
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        userSessions.put(userId, session);
        logger.info("User {} connected. Session ID: {}", userId, session.getId());
    }

    @OnMessage
    public void onMessage(String messageStr, Session session) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Parse the JSON string to a generic map to inspect the "type"
            Map<String, Object> payload = mapper.readValue(messageStr, Map.class);
            String type = (String) payload.get("type");

            if (type == null) {
                logger.warn("Received message without a 'type' field: {}", messageStr);
                return;
            }

            String recipientId = null;

            // Handle message type
            switch (type) {
                case "message":
                    recipientId = (payload.get("recipientId") instanceof Integer)
                            ? String.valueOf(payload.get("recipientId"))
                            : (String) payload.get("recipientId");

                    if (recipientId == null) {
                        logger.warn("Message type 'message' missing 'recipientId': {}", messageStr);
                        return;
                    }
                    handleMessage(recipientId, mapper.convertValue(payload, UserMessageWebSocket.class));
                    break;

                case "alarm":
                    recipientId = (payload.get("alarmUserId") instanceof Integer)
                            ? String.valueOf(payload.get("alarmUserId"))
                            : (String) payload.get("alarmUserId");

                    if (recipientId == null) {
                        logger.warn("Message type 'alarm' missing 'alarmUserId': {}", messageStr);
                        return;
                    }
                    handleAlarm(recipientId, mapper.convertValue(payload, AlarmMessageWebSocket.class));
                    break;

                default:
                    logger.warn("Unknown message type: {}", type);
            }
        } catch (IOException e) {
            logger.error("Error processing message: {}", messageStr, e);
        }
    }


    private void handleMessage(String recipientId, UserMessageWebSocket message) {
        Session recipientSession = userSessions.get(recipientId);
        if (recipientSession != null && recipientSession.isOpen()) {
            sendDirectMessage( recipientSession, message);
        } else {
            logger.warn("Recipient session not found or is closed for userId: {}", recipientId);
        }
    }

    private void handleAlarm(String alarmUserId, AlarmMessageWebSocket alarm) {
        Session recipientSession = userSessions.get(alarmUserId);
        if (recipientSession != null && recipientSession.isOpen()) {
            sendAlarm(recipientSession, alarm);
        } else {
            logger.warn("Recipient session not found or is closed for userId: {}", alarmUserId);
        }
    }

    private void sendAlarm(Session recipientSession, AlarmMessageWebSocket alarm) {
        try {
            recipientSession.getBasicRemote().sendObject(alarm);
        } catch (IOException | EncodeException e) {
            logger.error("Error sending message to user {}: {}", recipientSession, e.getMessage(), e);
        }
    }


    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        Session removedSession = userSessions.remove(userId);
        if (removedSession != null) {
            logger.info("User {} disconnected. Session ID: {}", userId, session.getId());
        }
    }

    @OnError
    public void onError(Session session, @PathParam("userId") String userId, Throwable error) {
        logger.error("WebSocket error for user {}: {}", userId, error.getMessage(), error);
        try {
            session.close();
        } catch (IOException e) {
            logger.error("Error closing session for user {}", userId, e);
        }
    }

    public void sendDirectMessage(Session recipientSession, UserMessageWebSocket message) {
        try {
            recipientSession.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            logger.error("Error sending message to user {}: {}", recipientSession, e.getMessage(), e);
        }
    }

    // Optional: Add method to check if a user is connected
    public boolean isUserConnected(String userId) {
        Session session = userSessions.get(userId);
        return session != null && session.isOpen();
    }
}
