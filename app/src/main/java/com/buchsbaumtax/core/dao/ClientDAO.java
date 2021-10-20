package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ClientDAO {

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM clients ORDER BY id")
    List<Client> getAll();

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM clients WHERE id = :id")
    Client get(@Bind("id") int id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO clients (last_name, current_status, periodical, display_name, display_phone) VALUES (:lastName, :currentStatus, :periodical, :displayName, :displayPhone)")
    int create(@BindBean Client client);

    @SqlUpdate("UPDATE clients SET last_name = :lastName, current_status = :currentStatus, periodical = :periodical, display_name = :displayName, display_phone = :displayPhone WHERE id = :id")
    void update(@BindBean Client client);

    @SqlUpdate("DELETE FROM clients WHERE id = :id")
    void delete(@Bind("id") int id);
}
