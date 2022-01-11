package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.ClientFlag;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@Dao
public interface ClientFlagDAO {
    @RegisterFieldMapper(ClientFlag.class)
    @SqlQuery("SELECT * FROM client_flags order by id")
    List<ClientFlag> getAll();
}
