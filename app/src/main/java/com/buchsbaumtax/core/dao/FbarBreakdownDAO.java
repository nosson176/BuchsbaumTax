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
    @SqlUpdate("INSERT INTO fbar_breakdowns (include, depend, description, documents, frequency, currency, amount, part, tax_type, tax_group, category, archived, client_id) VALUES (:include, :depend, :description, :documents, :frequency, :currency, :amount, :part, :taxType, :taxGroup, :category, :archived, :clientId)")
    int create(@BindBean FbarBreakdown fbarBreakdown);

    @SqlUpdate("UPDATE fbar_breakdowns SET include = :include, depend = :depend, description = :description, documents = :documents, frequency = :frequency, currency = :currency, amount = :amount, part = :part, tax_type = :taxType, tax_group = :taxGroup, category = :category, archived = :archived, client_id = :clientId WHERE id = :id")
    void update(@BindBean FbarBreakdown fbarBreakdown);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE id = :id")
    FbarBreakdown get(@Bind("id") int id);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns ORDER BY id")
    List<FbarBreakdown> getAll();

    @SqlUpdate("DELETE FROM fbar_breakdowns WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE client_id = :clientId")
    List<FbarBreakdown> getForClient(@Bind("clientId") int clientId);
}
