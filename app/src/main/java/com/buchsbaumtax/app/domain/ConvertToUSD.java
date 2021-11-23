package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sifradigital.framework.db.Database;

import java.util.Objects;

public class ConvertToUSD {
    static Cache<CurrencyYear, Double> cache = Caffeine.newBuilder().build();

    public static Double convertToUSD(double amount, String currency, String year) {
        if (currency != null && year != null) {
            if (currency.equals("USD")) {
                return amount;
            }
            CurrencyYear key = new CurrencyYear(year, currency);
            Double rate = cache.get(key, k -> getRate(k.getYear(), k.getCurrency()));
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

    private static class CurrencyYear {
        private String year;
        private String currency;

        public CurrencyYear(String year, String currency) {
            this.year = year;
            this.currency = currency;
        }

        public String getYear() {
            return year;
        }

        public String getCurrency() {
            return currency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CurrencyYear that = (CurrencyYear)o;
            return year.equals(that.year) && currency.equals(that.currency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, currency);
        }
    }
}
