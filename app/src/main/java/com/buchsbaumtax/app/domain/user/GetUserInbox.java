package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.dto.UserMessageObject;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;

import java.util.ArrayList;
import java.util.List;

public class GetUserInbox {

    public List<UserMessageObject> getUserInbox(User user) {
        List<UserMessage> messages = Database.dao(UserMessageDAO.class).getByUser(user.getId());

        List<UserMessageObject> results = new ArrayList<>();
        for (UserMessage message : messages) {
            if (message.getThreadId() != null) {
                if (results.stream().noneMatch(m -> m.getId() == message.getThreadId())) {
                    results.add(new UserMessageObject(Database.dao(UserMessageDAO.class).get(message.getThreadId())));
                }
            }
            else {
                results.add(new UserMessageObject(message));
            }
        }
       return results;
    }
}
