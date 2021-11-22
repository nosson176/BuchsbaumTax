package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface SmartviewDAO {
    @RegisterFieldMapper(Smartview.class)
    @SqlQuery("SELECT * FROM smartviews WHERE id = :id")
    Smartview get(@Bind("id") int id);

    @RegisterFieldMapper(Smartview.class)
    @SqlQuery("SELECT * FROM smartviews ORDER BY id")
    List<Smartview> getAll();

    @RegisterFieldMapper(SmartviewLine.class)
    @SqlQuery("SELECT * FROM smartview_lines WHERE smartview_id = :smartviewId ORDER BY id")
    List<SmartviewLine> getSmartviewLines(@Bind("smartviewId") int smartviewId);

    @SqlUpdate("UPDATE smartviews SET name = :name, sort_number = :sortNumber, archived = :archived, client_count = :clientCount, client_ids = :clientIds, updated = NOW() WHERE id = :id")
    void update(@BindBean Smartview smartview);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO smartviews (user_name, user_id, name, sort_number, archived) VALUES (:userName, :userId, :name, :sortNumber, :archived)")
    int create(@BindBean Smartview smartview);
}
