package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.IncomeBreakdown;
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
public interface IncomeBreakdownDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO income_breakdowns (client_id, years, category, tax_group, tax_type, job, currency, frequency, documents, description, amount, exclusion, include, archived, depend) VALUES (:clientId, :years, :category, :taxGroup, :taxType, :job, :currency, :frequency, :documents, :description, :amount, :exclusion, :include, :archived, :depend)")
    int create(@BindBean IncomeBreakdown incomeBreakdown);

    @SqlUpdate("UPDATE income_breakdowns SET years = :years, category = :category, tax_group = :taxGroup, tax_type = :taxType, job = :job, currency = :currency, frequency = :frequency, documents = :documents, description = :description, amount = :amount, exclusion = :exclusion, include = :include, archived = :archived, depend = :depend WHERE id = :id")
    void update(@BindBean IncomeBreakdown incomeBreakdown);

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT * FROM income_breakdowns WHERE id = :id")
    IncomeBreakdown get(@Bind("id") int id);

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT * FROM income_breakdowns ORDER BY id")
    List<IncomeBreakdown> getAll();

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT * FROM income_breakdowns WHERE client_id = :clientId ORDER BY years DESC")
    List<IncomeBreakdown> getForClient(@Bind("clientId") int clientId);

    @SqlBatch("UPDATE income_breakdowns SET years = :years, category = :category, tax_group = :taxGroup, tax_type = :taxType, job = :job, currency = :currency, frequency = :frequency, documents = :documents, description = :description, amount = :amount, exclusion = :exclusion, include = :include, archived = :archived, depend = :depend WHERE id = :id")
    void update(@BindBean List<IncomeBreakdown> incomeBreakdowns);
}
