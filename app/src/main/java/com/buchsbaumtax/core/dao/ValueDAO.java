package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Value;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ValueDAO {
    @RegisterFieldMapper(Value.class)
    @SqlQuery("SELECT * FROM value_lists ORDER BY key, sort_order")
    List<Value> getAll();

    @RegisterFieldMapper(Value.class)
    @SqlQuery("SELECT * FROM value_lists WHERE id = :id")
    Value get(@Bind("id") int id);

    @SqlUpdate("DELETE FROM value_lists WHERE id = :id")
    void delete(@Bind("id") int id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO value_lists (sort_order, key, value, parent_id, translation_needed, show, include) VALUES (:sortOrder, :key, :value, :parentId, :translationNeeded, :show, :include)")
    int create(@BindBean Value value);

    @SqlUpdate("UPDATE value_lists SET sort_order = :sortOrder, value = :value, parent_id = :parentId, show = :show, include = :include WHERE id = :id")
    void update(@BindBean Value value);

    @RegisterFieldMapper(Value.class)
    @SqlQuery("SELECT key FROM value_lists GROUP BY key ORDER BY key")
    List<String> getAllValueTypes();
}
