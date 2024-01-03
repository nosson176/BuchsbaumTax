package com.sifradigital.framework.mail.model;

import java.util.List;
import java.util.Map;

public class ResolvedSendInfo {

    private final String fromAddress;
    private final String fromName;

    private final String replyToAddress;
    private final String toAddress;
    private final String subject;
    private final String body;
    private final String contentType;
    private final List<Attachment> attachments;
    private final Map<String, String> mergeTags;

    ResolvedSendInfo(String fromAddress, String fromName, String replyToAddress, String toAddress, String subject, String body, String contentType, List<Attachment> attachments, Map<String, String> mergeTags) {
        this.fromAddress = fromAddress;
        this.fromName = fromName;
        this.replyToAddress = replyToAddress;
        this.toAddress = toAddress;
        this.subject = subject;
        this.body = body;
        this.contentType = contentType;
        this.attachments = attachments;
        this.mergeTags = mergeTags;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getFromName() {
        return fromName;
    }

    public String getReplyToAddress() {
        return replyToAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Map<String, String> getMergeTags() {
        return mergeTags;
    }
}
