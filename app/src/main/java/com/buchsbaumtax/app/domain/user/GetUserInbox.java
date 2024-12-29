package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.dto.UserMessageObject;
import com.buchsbaumtax.app.job.UserService;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GetUserInbox {
    private static final Logger logger = LoggerFactory.getLogger(GetUserInbox.class);

    public List<UserMessageObject> getUserInbox(User user) {
        List<UserMessage> messages = Database.dao(UserMessageDAO.class).getByUser(user.getId());
        List<UserMessageObject> results = new ArrayList<>();
        List<Integer> processedIds = new ArrayList<>(); // To track processed message/thread IDs

        for (UserMessage message : messages) {
            if (message.getThreadId() != null) {
                // Fetch the parent thread
                int threadId = message.getThreadId();
                if (!processedIds.contains(threadId)) {
                    UserMessage threadMessage = Database.dao(UserMessageDAO.class).get(threadId);
                    if (threadMessage != null) {
                        results.add(new UserMessageObject(threadMessage));
                        processedIds.add(threadId);
                    }
                }
            } else {
                // Handle standalone messages
                if (!processedIds.contains(message.getId())) {
                    results.add(new UserMessageObject(message));
                    processedIds.add(message.getId());
                }
            }
        }

//        logger.info("emails result: {}", results);
        return results;
    }
}

