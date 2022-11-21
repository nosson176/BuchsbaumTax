package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReceiveSMS {

    private static final Logger Log = LoggerFactory.getLogger(ReceiveSMS.class);

    public void receiveSMS(String body, String from) {
        String[] stringParts = body.split(":");

        if (stringParts.length < 2) {
            Log.error("Error creating message object: Message incorrectly formatted");
            return;
        }

        String forName = stringParts[0].toLowerCase().trim();
        List<User> users = Database.dao(UserDAO.class).getAll();
        User recipient = users.stream().filter(u -> u.getUsername().toLowerCase().equals(forName)).findAny().orElse(null);
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
