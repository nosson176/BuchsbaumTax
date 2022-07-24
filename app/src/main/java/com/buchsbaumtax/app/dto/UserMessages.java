package com.buchsbaumtax.app.dto;

import java.util.List;

public class UserMessages {
    List<Integer> recipients;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Integer> recipients) {
        this.recipients = recipients;
    }
}
