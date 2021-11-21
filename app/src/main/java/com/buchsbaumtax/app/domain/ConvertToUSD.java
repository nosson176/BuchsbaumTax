package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sifradigital.framework.db.Database;

public class ConvertToUSD {
    static Cache<String, Double> cache = Caffeine.newBuilder().build();

    public static Double convertToUSD(double amount, String currency, String year) {
        if (currency != null && year != null) {
            if (currency.equals("USD")) {
                return amount;
            }
            String key = currency + year;
            Double rate = cache.getIfPresent(key);
            if (rate == null) {
                ExchangeRate exchangeRate = Database.dao(ExchangeRateDAO.class).getForCurrencyYear(currency, year);
                if (exchangeRate != null) {
                    cache.put(key, exchangeRate.getRate());
                    rate = cache.getIfPresent(key);
                }
            }
            if (rate != null) {
                return amount / rate;
            }
        }
        return null;
    }
}
