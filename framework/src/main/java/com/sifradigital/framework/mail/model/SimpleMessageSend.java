package com.sifradigital.framework.mail.model;

import com.sifradigital.framework.mail.MailConfig;

public class SimpleMessageSend extends AbstractSend {

    private final String message;

    public SimpleMessageSend(String toAddress, String message) {
        super(null, null, toAddress, null);
        this.message = message;
    }

    @Override
    public ResolvedSendInfo resolve(MailConfig config) {
        String fromAddress = getFromAddress() != null ? getFromAddress() : config.getDefaultFromAddress();
        return new ResolvedSendInfo(fromAddress, null, null, getToAddress(), getSubject(), message, null, null, null);
    }
}
