package com.sifradigital.framework.mail.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sifradigital.framework.mail.MailService;
import com.sifradigital.framework.mail.model.Attachment;
import com.sifradigital.framework.mail.model.ResolvedSendInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SendgridEmailService implements MailService {

    private final static Logger Log = LoggerFactory.getLogger(SendgridEmailService.class);

    private final SendGrid sendGrid;

    public SendgridEmailService(String apiKey) {
        sendGrid = new SendGrid(apiKey);
    }

    @Override
    public boolean send(ResolvedSendInfo send) {
        Log.info("Sending email to {} with subject \"{}\"", send.getToAddress(), send.getSubject());
        Email from = new Email(send.getFromAddress(), send.getFromName());
        Email to = new Email(send.getToAddress());
        Mail mail = new Mail(from, send.getSubject(), to, new Content(send.getContentType(), send.getBody()));

        if (send.getReplyToAddress() != null) {
            mail.setReplyTo(new Email(send.getReplyToAddress()));
        }
        for (Attachment attachment : send.getAttachments()) {
            mail.addAttachments(createAttachment(attachment));
        }

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() < 400) {
                return true;
            }
            else {
                Log.error("Error sending email - Error code {}", response.getStatusCode());
                Log.error(response.getBody());
                return false;
            }
        }
        catch (IOException e) {
            Log.error("Error sending email", e);
            return false;
        }
    }

    private Attachments createAttachment(Attachment attachment) {
        return new Attachments
                .Builder(attachment.getName(), new ByteArrayInputStream(attachment.getData()))
                .withType(attachment.getType())
                .build();
    }
}
