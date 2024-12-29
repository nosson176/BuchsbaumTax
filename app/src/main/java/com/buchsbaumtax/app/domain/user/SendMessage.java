package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.UserMessageObject;
import com.buchsbaumtax.app.dto.UserMessages;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;

import java.util.ArrayList;
import java.util.List;

public class SendMessage {

    public BaseResponse sendMessages(User user, UserMessages messages) {
        List<UserMessageObject> createdMessages = new ArrayList<>();

        messages.getRecipients().forEach(recipientId -> {
            UserMessage createdMessage = createMessage(user, recipientId, messages);
            createdMessages.add(new UserMessageObject(createdMessage));
        });

        BaseResponse response = new BaseResponse(true);
        response.setData(createdMessages); // Add the created messages to the response
        return response;
    }

    private UserMessage createMessage(User user, int recipientId, UserMessages messages) {
        UserMessage userMessage = new UserMessage();
        userMessage.setRecipientId(recipientId);
        userMessage.setMessage(messages.getMessage());
        userMessage.setSenderId(user.getId());
        userMessage.setParentId(messages.getParentId());
        userMessage.setThreadId(messages.getThreadId());

        int id = Database.dao(UserMessageDAO.class).create(userMessage);
        return Database.dao(UserMessageDAO.class).get(id);
    }
}
