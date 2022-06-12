package com.buchsbaumtax.core.model;

import java.util.Date;

public class UserMessage {
    public static final String STATUS_UNREAD = "unread";
    public static final String STATUS_READ = "read";

    private long id;
    private long senderId;
    private long recipientId;
    private String message;
    private String status;
    private Date created;


    public long getId() {
        return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
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
}
