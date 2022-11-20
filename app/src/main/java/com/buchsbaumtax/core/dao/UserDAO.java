package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.create.UserCreate;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface UserDAO {

    @RegisterFieldMapper(User.class)
    @SqlQuery("SELECT * FROM users ORDER BY id")
    List<User> getAll();

    @RegisterFieldMapper(User.class)
    @SqlQuery("SELECT * FROM users WHERE id = :id")
    User get(@Bind("id") int id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO users (first_name, last_name, username, user_type, password, phone_number, send_login_notifications, notify_of_logins, seconds_in_day, allow_texting, selectable) VALUES (:firstName, :lastName, :username, :userType, :password, :phoneNumber, :sendLoginNotifications, :notifyOfLogins, :secondsInDay, :allowTexting, :selectable)")
    int create(@BindBean UserCreate userCreate);

    @SqlUpdate("UPDATE users SET first_name = :firstName, last_name = :lastName, username = :username, phone_number = :phoneNumber, send_login_notifications = :sendLoginNotifications, notify_of_logins = :notifyOfLogins, seconds_in_day = :secondsInDay, allow_texting = :allowTexting, selectable = :selectable, user_type = :userType, updated = now() WHERE id = :id")
    void update(@BindBean User user);

    @SqlUpdate("UPDATE users SET password = :password WHERE id = :id")
    void updatePassword(@Bind("id") int id, @Bind("password") String password);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(User.class)
    @SqlQuery("SELECT * FROM users WHERE username = :username")
    User getByUsername(@Bind("username") String username);

    @RegisterFieldMapper(User.class)
    @SqlQuery("SELECT u.* FROM users u JOIN sessions t ON (u.id = t.user_id) WHERE t.token = :token")
    User getByToken(@Bind("token") String token);
}
