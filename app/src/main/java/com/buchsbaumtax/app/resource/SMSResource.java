package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.domain.ReceiveSMS;
import com.buchsbaumtax.app.domain.SendSMS;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.model.SMSMessage;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/sms")
public class SMSResource {
    private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

    @POST
    @Path("/send")
    public BaseResponse sendSMS(@Authenticated User user, SMSMessage smsMessage) {
        logger.info("SMS IS here :", user, smsMessage);

        return new SendSMS().sendSMS(smsMessage);
    }

    @POST
    @Path("/webhook")
    public BaseResponse receiveSMS(@FormParam("Body") String body, @FormParam("From") String from) {
        new ReceiveSMS().receiveSMS(body, from);
        return new BaseResponse(true);
    }
}
