package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.User;
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
    @SqlUpdate("INSERT INTO users (first_name, last_name, email, user_type, created, updated) VALUES (:firstName, :lastName, :email, :userType,:created, :updated)")
    int create(@BindBean User user);

    @SqlUpdate("UPDATE users SET first_name = :firstName, last_name = :lastName, email = :email, user_type = :userType, updated = now() WHERE id = :id")
    void update(@BindBean User user);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(User.class)
    @SqlQuery("SELECT * FROM users WHERE email = :email")
    User getByEmail(@Bind("email") String email);
}
