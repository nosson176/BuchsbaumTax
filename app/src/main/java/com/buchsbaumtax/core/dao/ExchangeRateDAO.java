package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.ExchangeRate;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@Dao
public interface ExchangeRateDAO {
    @RegisterFieldMapper(ExchangeRate.class)
    @SqlQuery("SELECT * FROM exchange_rates ORDER BY id")
    List<ExchangeRate> getAll();
}
