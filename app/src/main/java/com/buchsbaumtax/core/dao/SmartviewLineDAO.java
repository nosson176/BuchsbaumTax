package com.buchsbaumtax.core.dao;

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
public interface SmartviewLineDAO {
    @RegisterFieldMapper(SmartviewLine.class)
    @SqlQuery("SELECT * FROM smartview_lines WHERE smartview_id = :smartviewId ORDER BY id")
    List<SmartviewLine> getForSmartview(@Bind("smartviewId") int smartviewId);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO smartview_lines (smartview_id, query, class_to_join, field_to_search, search_value, operator, type) VALUES (:smartviewId, :query, :classToJoin, :fieldToSearch, :searchValue, :operator, :type)")
    int create(@BindBean SmartviewLine smartviewLine);

    @SqlUpdate("UPDATE smartview_lines SET query = :query, class_to_join = :classToJoin, field_to_search = :fieldToSearch, search_value = :searchValue, operator = :operator, type = :type, updated = now() WHERE id = :id")
    void update(@BindBean SmartviewLine smartviewLine);
}
