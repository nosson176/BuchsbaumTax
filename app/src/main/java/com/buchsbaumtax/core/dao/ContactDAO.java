package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Contact;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ContactDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO contacts (client_id, contact_type, memo, main_detail, secondary_detail, state, zip, enabled, archived) VALUES (:clientId, :contactType, :memo, :mainDetail, :secondaryDetail, :state, :zip, :enabled, :archived)")
    int create(@BindBean Contact contact);

    @SqlUpdate("UPDATE contacts SET client_id = :clientId, memo = :memo, main_detail = :mainDetail, secondary_detail = :secondaryDetail, state = :state, zip = :zip, enabled = :enabled, archived = :archived WHERE id = :id")
    void update(@BindBean Contact contact);

    @RegisterFieldMapper(Contact.class)
    @SqlQuery("SELECT * FROM contacts WHERE id = :id")
    Contact get(@Bind("id") int id);

    @RegisterFieldMapper(Contact.class)
    @SqlQuery("SELECT * FROM contacts ORDER BY id")
    List<Contact> getAll();

    @SqlUpdate("DELETE FROM contacts WHERE id = :id")
    void delete(@Bind("id") int id);


    @RegisterFieldMapper(Contact.class)
    @SqlQuery("SELECT * FROM contacts WHERE client_id = :clientId")
    List<Contact> getForClient(@Bind("clientId") int clientId);
}
