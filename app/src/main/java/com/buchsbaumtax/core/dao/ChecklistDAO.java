package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Checklist;
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
public interface ChecklistDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO checklist_items (client_id, finished, memo, tax_year_id, translated, sort_number) VALUES (:clientId, :finished, :memo, :taxYearId, :translated, :sortNumber)")
    int create(@BindBean Checklist checklist);

    @RegisterFieldMapper(Checklist.class)
    @SqlQuery("SELECT * FROM checklist_items WHERE id = :checklistId")
    Checklist get(@Bind("checklistId") int checklistId);

    @RegisterFieldMapper(Checklist.class)
    @SqlQuery("SELECT * FROM checklist_items")
    List<Checklist> getAll();

    @SqlUpdate("UPDATE checklist_items set archived = :archived, client_id = :clientId, finished = :finished, memo = :memo, tax_year_id = :taxYearId, translated = :translated, sort_number = :sortNumber WHERE id = :id")
    void update(@BindBean Checklist checklist);

    @RegisterFieldMapper(Checklist.class)
    @SqlQuery("SELECT * FROM checklist_items WHERE client_id = :clientId")
    List<Checklist> getForClient(@Bind("clientId") int clientId);

    @SqlBatch("UPDATE checklist_items SET archived = :archived, client_id = :clientId, finished = :finished, memo = :memo, tax_year_id = :taxYearId, translated = :translated, sort_number = :sortNumber WHERE id = :id")
    void bulkUpdate(@BindBean List<Checklist> checklists);
}
