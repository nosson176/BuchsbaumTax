package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.ExchangeRate;
import com.buchsbaumtax.core.model.create.UserCreate;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ExchangeRateDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO exchange_rates (currency , year , show , rate) VALUES (:currency, :year, :show, :rate)")
    int create(@BindBean ExchangeRate exchangeRate);

    @RegisterFieldMapper(ExchangeRate.class)
    @SqlQuery("SELECT * FROM exchange_rates ORDER BY id")
    List<ExchangeRate> getAll();

    @RegisterFieldMapper(ExchangeRate.class)
    @SqlQuery("SELECT * FROM exchange_rates WHERE currency = :currency AND year = :year")
    ExchangeRate getForCurrencyYear(@Bind("currency") String currency, @Bind("year") String year);
}
