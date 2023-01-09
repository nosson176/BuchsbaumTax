package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.TaxGroup;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface TaxGroupDAO {

    @RegisterFieldMapper(TaxGroup.class)
    @SqlQuery("SELECT * FROM tax_groups ORDER BY id")
    List<TaxGroup> getAll();

    @RegisterFieldMapper(TaxGroup.class)
    @SqlQuery("SELECT * FROM tax_groups WHERE id = :id")
    TaxGroup get(@Bind("id") int id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO tax_groups (value, show, include, self_employment, passive, sub_type) VALUES (:value, :show, :include, :selfEmployment, :passive, :subType)")
    int create(@BindBean TaxGroup taxGroup);
}
