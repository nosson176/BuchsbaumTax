package com.buchsbaumtax.core.model;

import java.util.Date;

public class SMSMessage {

    private int id;
    private int phoneNumberId;
    private String message;
    private Date created;

    public int getId() {
        return id;
    }

    public int getPhoneNumberId() {
        return phoneNumberId;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreated() {
        return created;
    }
}
