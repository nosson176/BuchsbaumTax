package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.dao.mapper.ClientReducer;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.ClientFlag;
import com.buchsbaumtax.core.model.CustomerContactInfo;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.*;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.List;
import java.util.Set;

@Dao
public interface ClientDAO {

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf ON c.id = cf.client_id WHERE (:active IS FALSE OR c.active = :active) ORDER BY c.last_name")
    List<Client> getAll(@Bind("active") boolean active);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf ON c.id = cf.client_id WHERE c.id = :id AND (:active IS FALSE OR c.active = :active)")
    Client get(@Bind("id") int id, @Bind("active") boolean active);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM clients WHERE id = :id")
    List<Client> getClientById(@Bind("id") Long id);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf ON c.id = cf.client_id WHERE c.id IN (<ids>) AND (:active IS FALSE OR c.active = :active) ORDER BY c.last_name")
    List<Client> getBulk(@BindList("ids") List<Integer> ids, @Bind("active") boolean active);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT DISTINCT c.*, cf.flag FROM clients c " +
            "LEFT JOIN client_flags cf ON c.id = cf.client_id " +
            "LEFT JOIN contacts co ON c.id = co.client_id " +
            "LEFT JOIN tax_personals tp ON c.id = tp.client_id " +
            "WHERE (:active IS NULL OR c.active = :active) " +
            "AND (c.last_name ILIKE CONCAT('%', :q, '%') " +
            "OR co.memo ILIKE CONCAT('%', :q, '%') " +
            "OR co.main_detail ILIKE CONCAT('%', :q, '%') " +
            "OR tp.first_name ILIKE CONCAT('%', :q, '%') " +
            "OR tp.last_name ILIKE CONCAT('%', :q, '%') " +
            "OR tp.ssn ILIKE CONCAT('%', :q, '%')) " +  // <-- Added missing closing parenthesis
            "ORDER BY c.last_name")
    List<Client> getFiltered(@Bind("q") String q, @Bind("active") Boolean active);



//    @RegisterFieldMapper(Client.class)
//    @RegisterFieldMapper(ClientFlag.class)
//    @UseRowReducer(ClientReducer.class)
//    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf ON c.id = cf.client_id WHERE (:active IS FALSE OR c.active = :active) AND (c.last_name ILIKE CONCAT('%', :q, '%')) ORDER BY c.last_name")
//    List<Client> getFiltered(@Bind("q") String q, @Bind("active") boolean active);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @AllowUnusedBindings
    @SqlQuery("<query>")
    List<Client> getFilteredWithFields(@Define("query") String query);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("<query>")
    Set<Integer> getClientIdsByQuery(@Define("query") String query);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO clients (status, owes_status, periodical, last_name, archived, display_name, pmt_status, display_phone, g_flag, status_change_date, active) VALUES (:status, :owesStatus, :periodical, :lastName, :archived, :displayName, :pmtStatus, :displayPhone, :gFlag, :statusChangeDate, :active)")
    int create(@BindBean Client client);

    @SqlUpdate("UPDATE clients SET status = :status, owes_status = :owesStatus, periodical = :periodical, last_name = :lastName, archived = :archived, display_name = :displayName, pmt_status = :pmtStatus,  display_phone = :displayPhone, g_flag = :gFlag, status_change_date = :statusChangeDate, active = :active, updated = now() WHERE id = :id")
    void update(@BindBean Client client);

    @SqlUpdate("DELETE FROM clients WHERE id = :id")
    void delete(@Bind("id") int id);

    // New method to fetch contact info for a specific client
    @RegisterFieldMapper(CustomerContactInfo.class)
    @SqlQuery("SELECT contact_type, memo, main_detail FROM contacts WHERE client_id = :clientId")
    CustomerContactInfo getContactInfoForClient(@Bind("clientId") int clientId);

    @RegisterFieldMapper(Client.class)
    @RegisterFieldMapper(ClientFlag.class)
    @UseRowReducer(ClientReducer.class)
    @SqlQuery("SELECT * FROM clients c LEFT JOIN client_flags cf ON c.id = cf.client_id WHERE c.id IN (<ids>) AND (:active IS FALSE OR c.active = :active)")
    List<Client> getClientsByIds(@BindList("ids") List<Long> ids, @Bind("active") boolean active);
}
