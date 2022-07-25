package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.ClientFlag;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ClientFlagDAO {
    @RegisterFieldMapper(ClientFlag.class)
    @SqlQuery("SELECT * FROM client_flags order by id")
    List<ClientFlag> getAll();

    @SqlUpdate("UPDATE client_flags SET client_id = :clientId, user_id = :userId, flag = :flag WHERE id = :id")
    void updateFlag(@BindBean ClientFlag clientFlag);
}
