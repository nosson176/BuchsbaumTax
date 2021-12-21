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
    @SqlUpdate("INSERT INTO fbar_breakdowns (client_id, years, category, tax_group, tax_type, part, currency, frequency, documents, description, amount, depend, include, archived) VALUES (:clientId, :years, :category, :taxGroup, :taxType, :part, :currency, :frequency, :documents, :description, :amount, :depend, :include, :archived)")
    int create(@BindBean FbarBreakdown fbarBreakdown);

    @SqlUpdate("UPDATE fbar_breakdowns SET client_id = :clientId, years = :years, category = :category, tax_group = :taxGroup, tax_type = :taxType, part = :part, currency = :currency, frequency = :frequency, documents = :documents, description = :description, amount = :amount, depend = :depend, include = :include, archived = :archived WHERE id = :id")
    void update(@BindBean FbarBreakdown fbarBreakdown);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE id = :id")
    FbarBreakdown get(@Bind("id") int id);

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns ORDER BY id")
    List<FbarBreakdown> getAll();

    @RegisterFieldMapper(FbarBreakdown.class)
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE client_id = :clientId ORDER BY category DESC, tax_group")
    List<FbarBreakdown> getForClient(@Bind("clientId") int clientId);
}
