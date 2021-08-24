package com.buchsbaumtax.core.dao;

import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@Dao
public interface UserSessionDAO {

    @SqlUpdate("INSERT INTO sessions (token, user_id) VALUES (:token, :userId)")
    void create(@Bind("token") String token, @Bind("userId") int userId);
}
