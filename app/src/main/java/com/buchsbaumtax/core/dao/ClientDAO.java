package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
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

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT DISTINCT clients.* <from> <where> ORDER BY clients.id")
    List<Client> getFiltered(@Define("from") String from, @Define("where") String where);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM clients WHERE status ~ :q OR periodical ~ :q OR last_name ~ :q OR display_name ~ :q OR display_phone ~ :q")
    List<Client> getFiltered(@Bind("q") String q);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO clients (status, owes_status, periodical, last_name, archived, display_name, display_phone) VALUES (:status, :owesStatus, :periodical, :lastName, :archived, :displayName, :displayPhone)")
    int create(@BindBean Client client);

    @SqlUpdate("UPDATE clients SET status = :status, owes_status = :owesStatus, periodical = :periodical, last_name = :lastName, archived = :archived, display_name = :displayName, display_phone = :displayPhone, updated = now() WHERE id = :id")
    void update(@BindBean Client client);

    @SqlUpdate("DELETE FROM clients WHERE id = :id")
    void delete(@Bind("id") int id);
}
