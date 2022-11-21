package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ReceiveSMS;
import com.buchsbaumtax.app.domain.SendSMS;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.PhoneNumberDAO;
import com.buchsbaumtax.core.model.PhoneNumber;
import com.buchsbaumtax.core.model.SMSMessage;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

@Path("/sms")
public class SMSResource {

    @GET
    @Authenticated
    @Path("/phone_numbers")
    public List<PhoneNumber> getAllPhoneNumbers() {
        return Database.dao(PhoneNumberDAO.class).getAll();
    }

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

    @POST
    @Path("/webhook")
    public BaseResponse receiveSMS(@FormParam("Body") String body, @FormParam("From") String from) {
        new ReceiveSMS().receiveSMS(body, from);
        return new BaseResponse(true);
    }
}
