package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;

public class CreateMessage {
    public UserMessage createMessage(User user, UserMessage userMessage) {
        userMessage.setSenderId(user.getId());
        int id = Database.dao(UserMessageDAO.class).create(userMessage);
        return Database.dao(UserMessageDAO.class).get(id);
    }
}
