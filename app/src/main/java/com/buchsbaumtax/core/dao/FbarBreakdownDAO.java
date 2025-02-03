package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.FbarBreakdown;
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
public interface FbarBreakdownDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO fbar_breakdowns (client_id, years, category, tax_group, tax_type, part, currency, frequency, documents, description, amount, amountusd, depend, include, archived, created_by, user_id) " +
            "VALUES (:clientId, :years, :category, :taxGroup, :taxType, :part, :currency, :frequency, :documents, :description, :amount, :amountUSD, :depend, :include, :archived, :createdBy, :userId)")
    int create(@BindBean FbarBreakdown fbarBreakdown);

    @SqlUpdate("UPDATE fbar_breakdowns SET years = :years, category = :category, tax_group = :taxGroup, tax_type = :taxType, part = :part, currency = :currency, frequency = :frequency, documents = :documents, " +
            "description = :description, amount = :amount, depend = :depend, include = :include, archived = :archived, created_by = :createdBy, amountUSD = :amountUSD, user_id = :userId WHERE id = :id")
    void update(@BindBean FbarBreakdown fbarBreakdown);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE id = :id")
    FbarBreakdown get(@Bind("id") int id);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns ORDER BY id")
    List<FbarBreakdown> getAll();

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE client_id = :clientId ORDER BY years DESC")
    List<FbarBreakdown> getForClient(@Bind("clientId") int clientId);

//    @SqlBatch("UPDATE fbar_breakdowns SET years = :years, category = :category, tax_group = :taxGroup, tax_type = :taxType, part = :part, currency = :currency, frequency = :frequency, documents = :documents, " +
//            "description = :description, amount = :amount, depend = :depend, include = :include, archived = :archived, created_by = :createdBy, amountUSD = :amountUSD, user_id = :userId WHERE id = :id")
//    void update(@BindBean List<FbarBreakdown> fbarBreakdowns);
@RegisterFieldMapper(FbarBreakdown.class)
@SqlQuery("SELECT * FROM fbar_breakdowns WHERE client_id = :clientId AND created_time >= :threeYearsAgoStart ORDER BY created_time DESC")
List<FbarBreakdown> getFbarBreakdownsFromLastThreeYears(@Bind("clientId") int clientId, @Bind("threeYearsAgoStart") long threeYearsAgoStart);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE client_id = :clientId AND created_time < :threeYearsAgoStart ORDER BY created_time DESC")
    List<FbarBreakdown> getFbarBreakdownsBeforeLastThreeYears(@Bind("clientId") int clientId, @Bind("threeYearsAgoStart") long threeYearsAgoStart);

    @SqlUpdate("DELETE FROM fbar_breakdowns WHERE id = :id")
    void delete(@Bind("id") int id);
}
