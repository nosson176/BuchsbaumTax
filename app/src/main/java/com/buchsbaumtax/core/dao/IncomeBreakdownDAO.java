package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.FbarBreakdown;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.buchsbaumtax.core.model.Log;
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
    @SqlUpdate("INSERT INTO income_breakdowns (client_id, years, category, tax_group, tax_type, job, currency, frequency, documents, description, amount, amountUSD , exclusion, include, archived, depend, created_by, user_id) " +
            "VALUES (:clientId, :years, :category, :taxGroup, :taxType, :job, :currency, :frequency, :documents, :description, :amount, :amountUSD, :exclusion, :include, :archived, :depend, :createdBy, :userId)")
    int create(@BindBean IncomeBreakdown incomeBreakdown);

    @SqlUpdate("UPDATE income_breakdowns SET years = :years, category = :category, tax_group = :taxGroup, tax_type = :taxType, job = :job, currency = :currency, frequency = :frequency, documents = :documents, " +
            "description = :description, amount = :amount, amountUSD = :amountUSD, exclusion = :exclusion, include = :include, archived = :archived, depend = :depend, created_by = :createdBy, user_id = :userId WHERE id = :id")
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

    @SqlBatch("UPDATE income_breakdowns SET years = :years, category = :category, tax_group = :taxGroup, tax_type = :taxType, job = :job, currency = :currency, frequency = :frequency, documents = :documents, " +
            "description = :description, amount = :amount, amountUSD = :amountUSD, exclusion = :exclusion, include = :include, archived = :archived, depend = :depend, created_by = :createdBy, user_id = :userId WHERE id = :id")
    void update(@BindBean List<IncomeBreakdown> incomeBreakdowns);

//    @RegisterFieldMapper(IncomeBreakdown.class)
//    @SqlQuery("SELECT * FROM income_breakdowns WHERE client_id = :clientId AND created_time >= :threeYearsAgoStart ORDER BY created_time DESC")
//    List<IncomeBreakdown> getIncomeBreakdownsFromLastThreeYears(@Bind("clientId") int clientId, @Bind("threeYearsAgoStart") long threeYearsAgoStart);
//
//    @RegisterFieldMapper(IncomeBreakdown.class)
//    @SqlQuery("SELECT * FROM income_breakdowns WHERE client_id = :clientId AND created_time < :threeYearsAgoStart ORDER BY created_time DESC")
//    List<IncomeBreakdown> getIncomeBreakdownsBeforeLastThreeYears(@Bind("clientId") int clientId, @Bind("threeYearsAgoStart") long threeYearsAgoStart);

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT * FROM income_breakdowns WHERE client_id = :clientId ORDER BY created_time DESC LIMIT 20")
    List<IncomeBreakdown> getNewestIncomeBreakdowns(@Bind("clientId") int clientId);

    @RegisterFieldMapper(IncomeBreakdown.class)
    @SqlQuery("SELECT * FROM income_breakdowns WHERE client_id = :clientId AND id NOT IN (SELECT id FROM income_breakdowns WHERE client_id = :clientId ORDER BY created_time DESC LIMIT 20) ORDER BY created_time DESC")
    List<IncomeBreakdown> getRemainingIncomeBreakdowns(@Bind("clientId") int clientId);


    @SqlUpdate("DELETE FROM income_breakdowns WHERE id = :id")
    void delete(@Bind("id") int id);
}
