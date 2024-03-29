package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Contact;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ContactDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO contacts (client_id, contact_type, memo, main_detail, secondary_detail, state, zip, enabled, archived, sort_order) VALUES (:clientId, :contactType, :memo, :mainDetail, :secondaryDetail, :state, :zip, :enabled, :archived, :sortOrder)")
    int create(@BindBean Contact contact);

    @SqlUpdate("UPDATE contacts SET contact_type = :contactType, memo = :memo, main_detail = :mainDetail, secondary_detail = :secondaryDetail, state = :state, zip = :zip, enabled = :enabled, archived = :archived, sort_order = :sortOrder WHERE id = :id")
    void update(@BindBean Contact contact);

    @RegisterFieldMapper(Contact.class)
    @SqlQuery("SELECT * FROM contacts WHERE id = :id")
    Contact get(@Bind("id") int id);

    @RegisterFieldMapper(Contact.class)
    @SqlQuery("SELECT c.* FROM contacts c LEFT JOIN value_lists vl ON c.contact_type = vl.value ORDER BY c.sort_order, vl.sort_order")
    List<Contact> getAll();

    @RegisterFieldMapper(Contact.class)
    @SqlQuery("SELECT c.* FROM contacts c LEFT JOIN value_lists vl ON c.contact_type = vl.value AND vl.key = 'contact_type' WHERE c.client_id = :clientId ORDER BY c.sort_order, vl.sort_order NULLS FIRST")
    List<Contact> getForClient(@Bind("clientId") int clientId);

    @SqlBatch("UPDATE contacts SET contact_type = :contactType, memo = :memo, main_detail = :mainDetail, secondary_detail = :secondaryDetail, state = :state, zip = :zip, enabled = :enabled, archived = :archived, sort_order = :sortOrder WHERE id = :id")
    void update(@BindBean List<Contact> contacts);
}
