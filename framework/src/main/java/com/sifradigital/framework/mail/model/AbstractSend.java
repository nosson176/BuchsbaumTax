package com.sifradigital.framework.mail.model;

public abstract class AbstractSend implements Send {

    private final String fromAddress;
    private final String fromName;
    private final String toAddress;
    private final String subject;

    public AbstractSend(String fromAddress, String fromName, String toAddress, String subject) {
        this.fromAddress = fromAddress;
        this.fromName = fromName;
        this.toAddress = toAddress;
        this.subject = subject;
    }

    protected String getFromAddress() {
        return fromAddress;
    }

    protected String getFromName() {
        return fromName;
    }

    protected String getToAddress() {
        return toAddress;
    }

    protected String getSubject() {
        return subject;
    }
}
