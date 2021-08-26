package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.FbarBreakdown;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface FbarBreakdownDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO fbar_breakdowns (year_ids, year_name, include, depend, description, documents, frequency, amount, archived) VALUES (:yearIds, :yearName, :include, :depend, :description, :documents, :frequency, :amount, :archived)")
    int create(@BindBean FbarBreakdown fbarBreakdown);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE id = :id")
    FbarBreakdown get(@Bind("id") int id);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns ORDER BY id")
    List<FbarBreakdown> getAll();

    @SqlUpdate("DELETE FROM fbar_breakdowns WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT f.* FROM fbar_breakdowns f JOIN fbar_breakdown_tax_years f_t ON f.id = f_t.fbar_breakdown_id WHERE f_t.tax_year_id = :taxYearId")
    List<FbarBreakdown> getForTaxYear(@Bind("taxYearId") int taxYearId);
}
