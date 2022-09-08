package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.SendSMS;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.PhoneNumberDAO;
import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.PhoneNumber;
import com.buchsbaumtax.core.model.SMSMessage;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/sms")
public class SMSResource {
    @GET
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
    public BaseResponse receiveSMS(@FormParam("Body") String body, @FormParam("From") String From) {
        String[] stringParts = body.split(":");

        if (stringParts.length < 2) {
            throw new WebApplicationException("Message incorrectly formatted", Response.Status.BAD_REQUEST);
        }

        User recipient = Database.dao(UserDAO.class).getByUsername(stringParts[0].trim());

        if (recipient == null) {
            throw new WebApplicationException("User not found", Response.Status.BAD_REQUEST);
        }

        User sender = Database.dao(UserDAO.class).getByUsername("NOSSON");
        UserMessage userMessage = new UserMessage();
        userMessage.setRecipientId(recipient.getId());
        userMessage.setSenderId(sender.getId());
        userMessage.setMessage(stringParts[1].trim());
        Database.dao(UserMessageDAO.class).create(userMessage);

        return new BaseResponse(true);
    }
}
