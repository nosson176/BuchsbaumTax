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
    @SqlUpdate("INSERT INTO contacts (archived, zip, state, secondary_detail, main_detail, memo, contact_type, disabled, client_id) VALUES (:archived, :zip, :state, :secondaryDetail, :mainDetail, :memo, :contactType, :disabled, :clientId)")
    int create(@BindBean Contact contact);

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
