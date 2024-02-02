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
    @SqlQuery("SELECT * FROM client_flags")
    List<ClientFlag> getAll();

    @SqlUpdate("INSERT INTO client_flags (client_id, user_id, flag) VALUES (:clientId, :userId, :flag) ON CONFLICT (client_id, user_id) DO UPDATE SET flag = :flag")
    void upsert(@BindBean ClientFlag clientFlag);

    @SqlQuery("SELECT flag FROM client_flags WHERE user_id = :userId AND client_id = :clientId")
    Integer getFlagForUserClient(@Bind("userId") int userId, @Bind("clientId") int clientId);
}
