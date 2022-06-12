package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.PhoneNumberDAO;
import com.buchsbaumtax.core.model.PhoneNumber;
import com.buchsbaumtax.core.model.SMSLog;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.mail.SMS;
import com.sifradigital.framework.mail.model.SimpleMessageSend;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SendSMS {
    public BaseResponse sendSMS(SMSLog smsLog) {
        PhoneNumber number = Database.dao(PhoneNumberDAO.class).get(smsLog.getPhoneNumberId());

        if (number == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        SMS.sendLater(new SimpleMessageSend(number.getPhoneNumber(), smsLog.getMessage()));
        Database.dao(PhoneNumberDAO.class).createSMSLog(smsLog);
        return new BaseResponse(true);

    }
}
