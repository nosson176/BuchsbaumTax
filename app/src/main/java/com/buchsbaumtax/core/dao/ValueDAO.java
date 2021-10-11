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
    @SqlQuery("SELECT * FROM value_lists ORDER BY key, id")
    List<Value> getAll();

    @RegisterFieldMapper(Value.class)
    @SqlQuery("SELECT * FROM value_lists WHERE id = :id")
    Value get(@Bind("id") int id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO value_lists (sort_order, key, value, parent_id, translation_needed, passive, self_employment, show, sub_type, include) VALUES (:sortOrder, :key, :value, :parentId, :translationNeeded, :passive, :selfEmployment, :show, :subType, :include)")
    int create(@BindBean Value value);

    @RegisterFieldMapper(Value.class)
    @SqlQuery("SELECT key FROM value_lists GROUP BY key ORDER BY key")
    List<String> getAllValueTypes();
}
