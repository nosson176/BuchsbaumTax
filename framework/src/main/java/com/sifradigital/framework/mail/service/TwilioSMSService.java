package com.sifradigital.framework.mail.service;

import com.sifradigital.framework.mail.MailService;
import com.sifradigital.framework.mail.model.ResolvedSendInfo;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwilioSMSService implements MailService {

    private final static Logger Log = LoggerFactory.getLogger(TwilioSMSService.class);

    public TwilioSMSService(String accountSid, String authToken) {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public boolean send(ResolvedSendInfo send) {
        Log.info("Sending SMS to {}, from {}, with body: {}", send.getToAddress(), send.getFromAddress(),
                send.getBody());
        try {
            Message message = Message.creator(
                    new PhoneNumber(send.getToAddress()),
                    new PhoneNumber(send.getFromAddress()),
                    send.getBody()).create();

            if (message.getErrorCode() != null) {
                Log.error("Error sending SMS - {}", message.getErrorMessage());
                return false;
            }

            Log.info("SMS sent successfully to {}", send.getToAddress());
            return true;
        } catch (ApiException e) {
            Log.error("Twilio API exception: {}", e.getMessage(), e);
            return false;
        } catch (Exception e) {
            Log.error("Error sending SMS: {}", e.getMessage(), e);
            return false;
        }
    }
}
