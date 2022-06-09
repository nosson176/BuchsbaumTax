package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.UserMessage;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface UserMessageDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO user_messages (sender_id, recipient_id, message) VALUES (:senderId, :recipientId, :message)")
    int create(@BindBean UserMessage userMessage);

    @RegisterFieldMapper(UserMessage.class)
    @SqlQuery("SELECT * FROM user_messages WHERE id = :id")
    UserMessage get(@Bind("id") int id);

    @RegisterFieldMapper(UserMessage.class)
    @SqlQuery("SELECT * FROM user_messages WHERE recipient_id = :recipientId")
    List<UserMessage> getByRecipient(@Bind("recipientId") int recipientId);

    @SqlUpdate("UPDATE user_messages SET sender_id = :senderId, recipient_id = :recipientId, message = :message, status = :status WHERE id = :id")
    void update(@BindBean UserMessage userMessage);
}
