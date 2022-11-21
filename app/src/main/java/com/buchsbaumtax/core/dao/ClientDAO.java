package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.dao.mapper.ClientReducer;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.ClientFlag;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.List;
import java.util.Set;

@Dao
public interface ClientDAO {

    String MATCHING_CLIENTS = "matching_clients(id) AS (SELECT c.* FROM clients c WHERE last_name ILIKE CONCAT('%', :q, '%')), ";
    String MATCHING_CONTACTS = "matching_contacts AS (SELECT co.* FROM contacts co WHERE (main_detail, memo)::text ILIKE CONCAT('%', :q, '%')), ";
    String MATCHING_PERSONALS = "matching_personals AS (SELECT tp.* FROM tax_personals tp WHERE (first_name, last_name, ssn, informal)::text ILIKE CONCAT('%', :q, '%'))";
    String WITH = "WITH " + MATCHING_CLIENTS + MATCHING_CONTACTS + MATCHING_PERSONALS;
    String SELECT_CLIENTS = "SELECT c.*, cf.* FROM matching_clients c LEFT JOIN client_flags cf ON c.id = cf.client_id UNION ";
    String SELECT_CONTACTS = "SELECT c.*, cf.* FROM matching_contacts co JOIN clients c ON co.client_id = c.id LEFT JOIN client_flags cf ON c.id = cf.client_id UNION ";
    String SELECT_PERSONALS = "SELECT c.*, cf.* FROM matching_personals p JOIN clients c ON p.client_id = c.id LEFT JOIN client_flags cf ON c.id = cf.client_id";
    String SELECT = " SELECT * FROM(" + SELECT_CLIENTS + SELECT_CONTACTS + SELECT_PERSONALS + ") AS result ORDER BY c.last_name;";

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf on c.id = cf.client_id ORDER BY c.last_name")
    List<Client> getAll();

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf on c.id = cf.client_id WHERE c.id = :id")
    Client get(@Bind("id") int id);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf on c.id = cf.client_id AND c.id IN (<ids>) ORDER BY c.last_name")
    List<Client> getBulk(@BindList("ids") List<Integer> ids);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery(WITH + SELECT)
    List<Client> getFiltered(@Bind("q") String q);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("<query>")
    List<Client> getFilteredWithFields(@Define("query") String query);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("<query>")
    Set<Integer> getClientIdsByQuery(@Define("query") String query);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO clients (status, owes_status, periodical, last_name, archived, display_name, display_phone) VALUES (:status, :owesStatus, :periodical, :lastName, :archived, :displayName, :displayPhone)")
    int create(@BindBean Client client);

    @SqlUpdate("UPDATE clients SET status = :status, owes_status = :owesStatus, periodical = :periodical, last_name = :lastName, archived = :archived, display_name = :displayName, display_phone = :displayPhone, updated = now() WHERE id = :id")
    void update(@BindBean Client client);

    @SqlUpdate("DELETE FROM clients WHERE id = :id")
    void delete(@Bind("id") int id);
}
