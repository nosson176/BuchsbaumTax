package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.ClientFlag;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
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

    @SqlQuery("SELECT flag FROM client_flags WHERE user_id = :userId AND client_id = :clientId")
    Integer getFlagForUserClient(@Bind("userId") int userId, @Bind("clientId") int clientId);
}
