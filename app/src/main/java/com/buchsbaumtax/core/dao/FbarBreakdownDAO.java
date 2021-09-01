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
    @SqlUpdate("INSERT INTO fbar_breakdowns (include, depend, description, documents, frequency, currency_id, amount, part_id, tax_type_id, tax_group_id, category_id, archived, client_id) VALUES (:include, :depend, :description, :documents, :frequency, :currecnyId, :amount, :partId, :taxTypeId, :taxGroupId, :categoryId, :archived, :clientId)")
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
    @SqlQuery("SELECT * FROM fbar_breakdowns WHERE client_id = :clientId")
    List<FbarBreakdown> getForClient(@Bind("clientId") int clientId);
}
