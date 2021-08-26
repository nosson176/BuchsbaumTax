package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface IncomeBreakdownDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO income_breakdowns (year_ids, year_name, depend, include, exclusion, amount, description, documents, frequency, archived) VALUES (:yearIds, :yearName, :depend, :include, :exclusion, :amount, :description, :documents, :frequency, :archived)")
    int create(@BindBean IncomeBreakdown incomeBreakdown);

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT * FROM income_breakdowns WHERE id = :id")
    IncomeBreakdown get(@Bind("id") int id);

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT * FROM income_breakdowns ORDER BY id")
    List<IncomeBreakdown> getAll();

    @SqlUpdate("DELETE FROM income_breakdowns WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT i.* FROM income_breakdowns i JOIN income_breakdown_tax_years i_t ON i.id = i_t.income_breakdown_id WHERE i_t.tax_year_id = :taxYearId")
    List<IncomeBreakdown> getForTaxYear(@Bind("taxYearId") int taxYearId);
}
