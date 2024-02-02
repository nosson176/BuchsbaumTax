package com.buchsbaumtax.core.model;

import java.util.Date;

public class SMSMessage {

    private int id;
    private String phoneNumber;
    private String message;
    private Date created;

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreated() {
        return created;
    }
}
