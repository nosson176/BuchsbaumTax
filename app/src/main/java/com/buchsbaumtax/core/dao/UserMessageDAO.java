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
    @SqlUpdate("INSERT INTO user_messages (sender_id, recipient_id, message, parent_id, thread_id) VALUES (:senderId, :recipientId, :message, :parentId, :threadId)")
    int create(@BindBean UserMessage userMessage);

    @RegisterFieldMapper(UserMessage.class)
    @SqlQuery("SELECT * FROM user_messages WHERE id = :id")
    UserMessage get(@Bind("id") int id);

    @RegisterFieldMapper(UserMessage.class)
//    @SqlQuery("SELECT * FROM user_messages WHERE recipient_id = :id ORDER BY created DESC")
    @SqlQuery("SELECT * FROM user_messages WHERE recipient_id = :id OR sender_id = :id ORDER BY created DESC")
    List<UserMessage> getByUser(@Bind("id") int id);

    @RegisterFieldMapper(UserMessage.class)
    @SqlQuery("SELECT * FROM user_messages WHERE thread_id = :parentId ORDER BY created")
    List<UserMessage> getByThread(@Bind("parentId") int parentId);

    @SqlUpdate("UPDATE user_messages SET message = :message, status = :status WHERE id = :id")
    void update(@BindBean UserMessage userMessage);

    @SqlUpdate("DELETE FROM user_messages WHERE id = :id")
    void delete(@Bind("id") int id);
}
