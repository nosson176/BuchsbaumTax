package com.buchsbaumtax.core.model;

import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.sifradigital.framework.db.Database;

import java.util.Date;
import java.util.List;

public class UserMessageWebSocket {

    public static final String STATUS_UNREAD = "unread";
    public static final String STATUS_READ = "read";

    private String type;
    private int id;
    private int senderId;
    private int recipientId;
    private String message;
    private String status;
    private Date created;
    private Integer parentId;
    private Integer threadId;
    private List<UserMessage> responses;

    public UserMessageWebSocket() {
    }

    public UserMessageWebSocket(UserMessage userMessage) {
        this.id = userMessage.getId();
        this.senderId = userMessage.getSenderId();
        this.recipientId = userMessage.getRecipientId();
        this.message = userMessage.getMessage();
        this.status = userMessage.getStatus();
        this.created = userMessage.getCreated();
        this.responses = Database.dao(UserMessageDAO.class).getByThread(userMessage.getId());
    }

    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessage() {
        return message;
    }
    public String getType() {
        return type;
    }
    public String setType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public List<UserMessage> getResponses() {
        return responses;
    }

    public void setResponses(List<UserMessage> responses) {
        this.responses = responses;
    }
}