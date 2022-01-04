package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.*;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ClientDAO {
    final String MATCHING_CLIENTS = "matching_clients(id) AS (SELECT c.* FROM clients c WHERE last_name ~ (:q)), ";
    final String MATCHING_CONTACTS = "matching_contacts AS (SELECT co.* FROM contacts co WHERE (main_detail, memo)::text ~ (:q)), ";
    final String MATCHING_PERSONALS = "matching_personals AS (SELECT tp.* FROM tax_personals tp WHERE (first_name, last_name, ssn, informal)::text ~ (:q))";
    final String WITH = "WITH " + MATCHING_CLIENTS + MATCHING_CONTACTS + MATCHING_PERSONALS;
    final String SELECT_CLIENTS = "SELECT c.* FROM matching_clients c UNION ";
    final String SELECT_CONTACTS = "SELECT c.* FROM matching_contacts co JOIN clients c ON co.client_id = c.id UNION ";
    final String SELECT_PERSONALS = "SELECT c.* FROM matching_personals p JOIN clients c ON p.client_id = c.id";
    final String SELECT = " SELECT * FROM(" + SELECT_CLIENTS + SELECT_CONTACTS + SELECT_PERSONALS + ") AS result ORDER BY id;";

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM clients ORDER BY id")
    List<Client> getAll();

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM clients WHERE id = :id")
    Client get(@Bind("id") int id);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM clients WHERE id IN (<ids>)")
    List<Client> getBulk(@BindList("ids") List<Integer> ids);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT DISTINCT clients.* <from> <where> ORDER BY clients.id")
    List<Client> getFiltered(@Define("from") String from, @Define("where") String where);

    @RegisterFieldMapper(Client.class)
    @AllowUnusedBindings
    @SqlQuery(WITH + SELECT)
    List<Client> getFiltered(@Bind("q") String q);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("<query>")
    List<Client> getFilteredWithFields(@Define("query") String query);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO clients (status, owes_status, periodical, last_name, archived, display_name, display_phone) VALUES (:status, :owesStatus, :periodical, :lastName, :archived, :displayName, :displayPhone)")
    int create(@BindBean Client client);

    @SqlUpdate("UPDATE clients SET status = :status, owes_status = :owesStatus, periodical = :periodical, last_name = :lastName, archived = :archived, display_name = :displayName, display_phone = :displayPhone, updated = now() WHERE id = :id")
    void update(@BindBean Client client);
}
