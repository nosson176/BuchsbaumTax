package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.ExchangeRate;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@Dao
public interface ExchangeRateDAO {
    @RegisterFieldMapper(ExchangeRate.class)
    @SqlQuery("SELECT * FROM exchange_rates WHERE currency = :currency AND year = :year")
    ExchangeRate getForCurrencyYear(@Bind("currency") String currency, @Bind("year") String year);
}
