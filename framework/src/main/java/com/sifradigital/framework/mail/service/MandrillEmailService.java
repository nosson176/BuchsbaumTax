package com.sifradigital.framework.mail.service;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.sifradigital.framework.mail.MailService;
import com.sifradigital.framework.mail.model.Attachment;
import com.sifradigital.framework.mail.model.ResolvedSendInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MandrillEmailService implements MailService {

    private final static Logger Log = LoggerFactory.getLogger(MandrillEmailService.class);

    private final MandrillApi mandrillApi;

    private String templateName;
    private String contentTagName;

    public MandrillEmailService(String apiKey) {
        this.mandrillApi = new MandrillApi(apiKey);
    }

    public MandrillEmailService(String apiKey, String templateName, String contentTagName) {
        this.mandrillApi = new MandrillApi(apiKey);
        this.templateName = templateName;
        this.contentTagName = contentTagName;
    }

    @Override
    public boolean send(ResolvedSendInfo resolvedSendInfo) {
        Log.info("Sending email to {} with subject \"{}\"", resolvedSendInfo.getToAddress(), resolvedSendInfo.getSubject());

        MandrillMessage message = new MandrillMessage();
        message.setSubject(resolvedSendInfo.getSubject());
        message.setFromEmail(resolvedSendInfo.getFromAddress());
        message.setFromName(resolvedSendInfo.getFromName());
        if (resolvedSendInfo.getReplyToAddress() != null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Reply-To", resolvedSendInfo.getReplyToAddress());
            message.setHeaders(headers);
        }

        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(resolvedSendInfo.getToAddress());
        recipients.add(recipient);
        message.setTo(recipients);

        if (!resolvedSendInfo.getAttachments().isEmpty()) {
            List<MandrillMessage.MessageContent> attachments = new ArrayList<>();
            for (Attachment attachment : resolvedSendInfo.getAttachments()) {
                MandrillMessage.MessageContent messageContent = new MandrillMessage.MessageContent();
                messageContent.setContent(Base64.getEncoder().encodeToString(attachment.getData()));
                messageContent.setType(attachment.getType());
                messageContent.setName(attachment.getName());
                attachments.add(messageContent);
            }
            message.setAttachments(attachments);
        }

        if (resolvedSendInfo.getMergeTags() != null && !resolvedSendInfo.getMergeTags().isEmpty()) {
            List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();
            for (String key : resolvedSendInfo.getMergeTags().keySet()) {
                MandrillMessage.MergeVar mergeVar = new MandrillMessage.MergeVar();
                mergeVar.setName(key);
                mergeVar.setContent(resolvedSendInfo.getMergeTags().get(key));
                mergeVars.add(mergeVar);
            }
            message.setGlobalMergeVars(mergeVars);
        }

        if (templateName != null) {
            return sendTemplated(message, resolvedSendInfo.getBody());
        }
        else {
            return send(message, resolvedSendInfo.getBody());
        }
    }

    private boolean sendTemplated(MandrillMessage mandrillMessage, String body) {
        try {
            Map<String, String> content = new HashMap<>();
            content.put(contentTagName, body);
            MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().sendTemplate(templateName, content, mandrillMessage, false);

            return messageStatusReports[0].getStatus().equals("sent");
        }
        catch (Exception e) {
            Log.error("Error sending email", e);
            return false;
        }
    }

    private boolean send(MandrillMessage mandrillMessage, String body) {
        try {
            mandrillMessage.setHtml(body);
            MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(mandrillMessage, false);
            return messageStatusReports[0].getStatus().equals("sent");
        }
        catch (Exception e) {
            Log.error("Error sending email", e);
            return false;
        }
    }
}
