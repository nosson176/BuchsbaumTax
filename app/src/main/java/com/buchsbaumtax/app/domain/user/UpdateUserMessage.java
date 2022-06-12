package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;

public class UpdateUserMessage {
    public UserMessage updateMessage(int messageId, UserMessage userMessage) {
        UserMessage message = Database.dao(UserMessageDAO.class).get(messageId);
        message.setStatus(userMessage.getStatus());

        Database.dao(UserMessageDAO.class).update(message);
        return Database.dao(UserMessageDAO.class).get(messageId);
    }
}
