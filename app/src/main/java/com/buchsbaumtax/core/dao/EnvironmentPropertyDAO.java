package com.buchsbaumtax.core.dao;

import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@Dao
public interface EnvironmentPropertyDAO {

    @SqlQuery("SELECT property_value FROM environment_properties WHERE property_key = :key")
    String valueForKey(@Bind("key") String key);
}
