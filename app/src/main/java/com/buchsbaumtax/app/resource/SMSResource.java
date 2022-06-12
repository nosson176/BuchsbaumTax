package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.SendSMS;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.PhoneNumberDAO;
import com.buchsbaumtax.core.model.PhoneNumber;
import com.buchsbaumtax.core.model.SMSMessage;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/sms")
public class SMSResource {
    @POST
    @Path("/phone_numbers")
    public PhoneNumber createPhoneNumber(@Authenticated User user, PhoneNumber phoneNumber) {
        int id = Database.dao(PhoneNumberDAO.class).create(phoneNumber);
        return Database.dao(PhoneNumberDAO.class).get(id);
    }

    @DELETE
    @Path("/phone_numbers/{phoneNumberId}")
    public BaseResponse deletePhoneNumber(@Authenticated User user, @PathParam("phoneNumberId") int phoneNumberId) {
        Database.dao(PhoneNumberDAO.class).delete(phoneNumberId);
        return new BaseResponse(true);
    }

    @POST
    @Path("/send")
    public BaseResponse sendSMS(@Authenticated User user, SMSMessage smsMessage) {
        return new SendSMS().sendSMS(smsMessage);
    }
}
