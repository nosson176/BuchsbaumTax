package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.SmartviewLine;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@Dao
public interface SmartviewDAO {

    @RegisterFieldMapper(SmartviewLine.class)
    @SqlQuery("SELECT * FROM smartview_lines WHERE smartview_id = :smartviewId ORDER BY id")
    List<SmartviewLine> getSmartviewLines(@Bind("smartviewId") int smartviewId);
}
