package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface TaxYearDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO tax_years (client_id, year, archived, irs_history) VALUES (:clientId, :year, :archived, :irsHistory)")
    int create(@BindBean TaxYear taxYear, @Bind("clientId") int clientId);

    @RegisterFieldMapper(TaxYear.class)
    @SqlQuery("SELECT * FROM tax_years WHERE id = :id")
    TaxYear get(@Bind("id") int id);

    @RegisterFieldMapper(TaxYear.class)
    @SqlQuery("SELECT * FROM tax_years WHERE client_id = :clientId")
    List<TaxYear> getByClient(@Bind("clientId") int clientId);

    @SqlUpdate("DELETE FROM tax_years WHERE id = :id")
    void delete(@Bind("id") int id);
}
