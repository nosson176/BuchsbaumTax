package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.dto.UserMessageObject;
import com.buchsbaumtax.core.dao.UserMessageDAO;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import java.util.List;
import java.util.stream.Collectors;

public class GetUserInbox {

    public List<UserMessageObject> getUserInbox(User user) {
        return Database.dao(UserMessageDAO.class).getByUser(user.getId())
                .stream()
                .filter(m -> m.getParentId() == null)
                .map(UserMessageObject::new)
                .collect(Collectors.toList());
    }
}
