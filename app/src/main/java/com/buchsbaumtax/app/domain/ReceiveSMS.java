package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveSMS {

    private static final Logger Log = LoggerFactory.getLogger(ReceiveSMS.class);

    public void receiveSMS(String body, String from) {
        String[] stringParts = body.split(":");

        if (stringParts.length < 2) {
            Log.error("Error creating message object: Message incorrectly formatted");
            return;
        }

        User recipient = Database.dao(UserDAO.class).getByUsername(stringParts[0].trim());

        if (recipient == null) {
            Log.error("Error creating message object: Recipient not found");
            return;
        }

        User sender = Database.dao(UserDAO.class).getByUsername("NOSSON");
        UserMessage userMessage = new UserMessage();
        userMessage.setRecipientId(recipient.getId());
        userMessage.setSenderId(sender.getId());
        userMessage.setMessage(stringParts[1].trim());
        Database.dao(UserMessageDAO.class).create(userMessage);
    }

}
