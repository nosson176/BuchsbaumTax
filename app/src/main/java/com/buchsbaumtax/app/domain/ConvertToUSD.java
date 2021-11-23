package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.ExchangeRateObject;
import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sifradigital.framework.db.Database;

public class ConvertToUSD {
    static Cache<ExchangeRateObject, Double> cache = Caffeine.newBuilder().build();

    public static Double convertToUSD(double amount, String currency, String year) {
        if (currency != null && year != null) {
            if (currency.equals("USD")) {
                return amount;
            }
            ExchangeRateObject key = new ExchangeRateObject(year, currency);
            Double rate = cache.get(key, k -> getRate(year, currency));
            if (rate != null) {
                return amount / rate;
            }
        }
        return null;
    }

    private static Double getRate(String year, String currency) {
        ExchangeRate exchangeRate = Database.dao(ExchangeRateDAO.class).getForCurrencyYear(currency, year);
        if (exchangeRate != null) {
            return exchangeRate.getRate();
        }
        return null;
    }
}
