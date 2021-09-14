package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface TaxPersonalDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO tax_personals (archived, client_id, category, first_name, middle_initial, last_name, date_of_birth, ssn, informal, relation, language, include) VALUES (:archived, :clientId, :category, :firstName, :middleInitial, :lastName, :dateOfBirth, :ssn, :informal, :relation, :language, :include)")
    int create(@BindBean TaxPersonal taxPersonal);

    @RegisterFieldMapper(TaxPersonal.class)
    @SqlQuery("SELECT * FROM tax_personals WHERE id = :id")
    TaxPersonal get(@Bind("id") int id);

    @RegisterFieldMapper(TaxPersonal.class)
    @SqlQuery("SELECT * FROM tax_personals ORDER BY id")
    List<TaxPersonal> getAll();

    @SqlUpdate("DELETE FROM tax_personals WHERE id = :id")
    void delete(@Bind("id") int id);


    @RegisterFieldMapper(TaxPersonal.class)
    @SqlQuery("SELECT * FROM tax_personals WHERE client_id = :clientId")
    List<TaxPersonal> getForClient(@Bind("clientId") int clientId);

}
