package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.TaxPersonal;
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
public interface TaxPersonalDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO tax_personals (client_id, category, include, language, relation, first_name, middle_initial, last_name, date_of_birth, ssn, informal, archived) VALUES (:clientId, :category, :include, :language, :relation, :firstName, :middleInitial, :lastName, :dateOfBirth, :ssn, :informal, :archived)")
    int create(@BindBean TaxPersonal taxPersonal);

    @SqlUpdate("UPDATE tax_personals SET category = :category, include = :include, language = :language, relation = :relation, first_name = :firstName, middle_initial = :middleInitial, last_name = :lastName, date_of_birth = :dateOfBirth, ssn = :ssn, informal = :informal, archived = :archived WHERE id = :id")
    void update(@BindBean TaxPersonal taxPersonal);

    @RegisterFieldMapper(TaxPersonal.class)
    @SqlQuery("SELECT * FROM tax_personals WHERE id = :id")
    TaxPersonal get(@Bind("id") int id);

    @RegisterFieldMapper(TaxPersonal.class)
    @SqlQuery("SELECT * FROM tax_personals ORDER BY id")
    List<TaxPersonal> getAll();


    @RegisterFieldMapper(TaxPersonal.class)
    @SqlQuery("SELECT * FROM tax_personals WHERE client_id = :clientId ORDER BY category DESC, first_name")
    List<TaxPersonal> getForClient(@Bind("clientId") int clientId);

    @SqlBatch("UPDATE tax_personals SET category = :category, include = :include, language = :language, relation = :relation, first_name = :firstName, middle_initial = :middleInitial, last_name = :lastName, date_of_birth = :dateOfBirth, ssn = :ssn, informal = :informal, archived = :archived WHERE id = :id")
    void update(@BindBean List<TaxPersonal> taxPersonals);

}
