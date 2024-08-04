package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.PhoneNumberDAO;
import com.buchsbaumtax.core.model.SMSMessage;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.mail.SMS;
import com.sifradigital.framework.mail.model.SimpleMessageSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SendSMS {

    private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

    public BaseResponse sendSMS(SMSMessage smsMessage) {

        if (smsMessage.getPhoneNumber() == null) {
            logger.info("getPhoneNumber function fail! :", smsMessage);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        boolean success = SMS.send(new SimpleMessageSend(smsMessage.getPhoneNumber(), smsMessage.getMessage()));

        if (success)
            logger.info("getPhoneNumber function success!!!! :", success);

        Database.dao(PhoneNumberDAO.class).createSMSLog(smsMessage);

        return new BaseResponse(success);
    }
}
