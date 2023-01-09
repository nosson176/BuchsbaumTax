package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.PhoneNumberDAO;
import com.buchsbaumtax.core.model.SMSMessage;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.mail.SMS;
import com.sifradigital.framework.mail.model.SimpleMessageSend;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SendSMS {

    public BaseResponse sendSMS(SMSMessage smsMessage) {

        if (smsMessage.getPhoneNumber() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        boolean success = SMS.send(new SimpleMessageSend(smsMessage.getPhoneNumber(), smsMessage.getMessage()));

        if (success)
            Database.dao(PhoneNumberDAO.class).createSMSLog(smsMessage);

        return new BaseResponse(success);
    }
}
