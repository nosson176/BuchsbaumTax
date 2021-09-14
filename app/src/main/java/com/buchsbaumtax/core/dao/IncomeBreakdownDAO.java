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
    @SqlUpdate("INSERT INTO income_breakdowns (depend, include, client_id, exclusion, amount, description, documents, frequency, currency, job, tax_type, tax_group, category, archived) VALUES (:depend, :include, :clientId, :exclusion, :amount, :description, :documents, :frequency, :currency, :job, :taxType, :taxGrouop, :category, :archived)")
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
    @SqlQuery("SELECT * FROM income_breakdowns WHERE client_id = :clientId")
    List<IncomeBreakdown> getForClient(@Bind("clientId") int clientId);
}
