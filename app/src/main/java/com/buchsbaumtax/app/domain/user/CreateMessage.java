package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.UserMessages;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;

public class CreateMessage {
    public BaseResponse postMessages(User user, UserMessages messages) {
        messages.getRecipients().forEach(r -> createMessage(user, r, messages.getMessage()));
        return new BaseResponse(true);
    }

    private UserMessage createMessage(User user, int recipientId, String message) {
        UserMessage userMessage = new UserMessage();
        userMessage.setRecipientId(recipientId);
        userMessage.setMessage(message);
        userMessage.setSenderId(user.getId());
        int id = Database.dao(UserMessageDAO.class).create(userMessage);
        return Database.dao(UserMessageDAO.class).get(id);
    }
}
