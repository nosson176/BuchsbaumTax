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
    final String MATCHING_CLIENTS = "matching_clients(id) AS (SELECT c.* FROM clients c, words WHERE to_tsquery('simple', w) @@ to_tsvector('simple', CONCAT_WS(' ', status, owes_status, periodical, last_name, display_name, display_phone))), ";
    final String MATCHING_FILINGS = "matching_filings AS (SELECT f.* FROM filings f, words WHERE to_tsquery('simple', w) @@ to_tsvector('simple', CONCAT_WS(' ', currency, memo, state, filing_type, file_type, status_detail, status, tax_form))), ";
    final String MATCHING_FBARS = "matching_fbars AS (SELECT fb.* FROM fbar_breakdowns fb, words WHERE to_tsquery('simple', w) @@ to_tsvector('simple', CONCAT_WS(' ', depend, description, documents, currency, part, tax_type, tax_group, category))), ";
    final String MATCHING_INCOMES = "matching_incomes AS (SELECT ib.* FROM income_breakdowns ib, words WHERE to_tsquery('simple', w) @@ to_tsvector('simple', CONCAT_WS(' ',depend, description, documents, currency, job, tax_type, tax_group, category))), ";
    final String MATCHING_LOGS = "matching_logs AS (SELECT l.* FROM logs l, words WHERE to_tsquery('simple', w) @@ to_tsvector('simple', CONCAT_WS(' ', alarm_time, note))), ";
    final String MATCHING_CONTACTS = "matching_contacts AS (SELECT co.* FROM contacts co, words WHERE to_tsquery('simple', w) @@ to_tsvector('simple', CONCAT_WS(' ', contact_type, main_detail, secondary_detail, memo, state, zip))), ";
    final String MATCHING_PERSONALS = "matching_personals AS (SELECT tp.* FROM tax_personals tp, words WHERE to_tsquery('simple', w) @@ to_tsvector('simple', CONCAT_WS(' ', category, first_name, middle_initial, last_name, ssn, informal, relation, language)))";
    final String WITH = "WITH words(w) AS (VALUES (:q)), " + MATCHING_CLIENTS + MATCHING_FILINGS + MATCHING_FBARS + MATCHING_INCOMES + MATCHING_LOGS + MATCHING_CONTACTS + MATCHING_PERSONALS;
    final String SELECT_CLIENTS = "SELECT c.* FROM matching_clients c UNION ";
    final String SELECT_FILINGS = "SELECT c.* FROM matching_filings f JOIN tax_years ty ON f.tax_year_id = ty.id JOIN clients c on ty.client_id = c.id UNION ";
    final String SELECT_FBARS = "SELECT c.* FROM matching_fbars fb JOIN clients c ON fb.client_id = c.id UNION ";
    final String SELECT_INCOMES = "SELECT c.* FROM matching_incomes ib JOIN clients c ON ib.client_id = c.id UNION ";
    final String SELECT_LOGS = "SELECT c.* FROM matching_logs l JOIN clients c ON l.client_id = c.id UNION ";
    final String SELECT_CONTACTS = "SELECT c.* FROM matching_contacts co JOIN clients c ON co.client_id = c.id UNION ";
    final String SELECT_PERSONALS = "SELECT c.* FROM matching_personals p JOIN clients c ON p.client_id = c.id";
    final String SELECT = " SELECT * FROM(" + SELECT_CLIENTS + SELECT_FILINGS + SELECT_FBARS + SELECT_INCOMES + SELECT_LOGS + SELECT_CONTACTS + SELECT_PERSONALS + ") AS result ORDER BY id;";

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

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO clients (status, owes_status, periodical, last_name, archived, display_name, display_phone) VALUES (:status, :owesStatus, :periodical, :lastName, :archived, :displayName, :displayPhone)")
    int create(@BindBean Client client);

    @SqlUpdate("UPDATE clients SET status = :status, owes_status = :owesStatus, periodical = :periodical, last_name = :lastName, archived = :archived, display_name = :displayName, display_phone = :displayPhone, updated = now() WHERE id = :id")
    void update(@BindBean Client client);
}
