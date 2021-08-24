package com.buchsbaumtax.core.dao;

import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@Dao
public interface UserPasswordDAO {

    @SqlUpdate("UPDATE users SET password = :password WHERE id = :id")
    void update(@Bind("id") int id, @Bind("password") String password);

    @SqlQuery("SELECT password FROM users WHERE id = :id")
    String get(@Bind("id") int id);
}
