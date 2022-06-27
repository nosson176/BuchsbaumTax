package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.TaxYear;
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
public interface TaxYearDAO {

    @RegisterFieldMapper(TaxYear.class)
    @SqlQuery("SELECT * FROM tax_years ORDER BY id")
    List<TaxYear> getAll();

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO tax_years (client_id, year, archived, irs_history) VALUES (:clientId, :year, :archived, :irsHistory)")
    int create(@BindBean TaxYear taxYear);

    @RegisterFieldMapper(TaxYear.class)
    @SqlQuery("SELECT * FROM tax_years WHERE id = :id")
    TaxYear get(@Bind("id") int id);

    @RegisterFieldMapper(TaxYear.class)
    @SqlQuery("SELECT * FROM tax_years WHERE client_id = :clientId ORDER BY year DESC, id DESC")
    List<TaxYear> getByClient(@Bind("clientId") int clientId);

    @SqlUpdate("UPDATE tax_years SET year = :year, archived = :archived, irs_history = :irsHistory WHERE id = :id")
    void update(@BindBean TaxYear taxYear);

    @SqlBatch("UPDATE tax_years SET year = :year, archived = :archived, irs_history = :irsHistory WHERE id = :id")
    void update(@BindBean List<TaxYear> taxYears);
}
