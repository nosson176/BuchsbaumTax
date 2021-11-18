package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.sifradigital.framework.db.Database;

public class ConvertToUSD {
    public static Double convertToUSD(double amount, String currency, String year) {
        if (currency != null && year != null) {
            if (currency.equals("USD")) {
                return amount;
            }
            ExchangeRate exchangeRate = Database.dao(ExchangeRateDAO.class).getForCurrencyYear(currency, year);
            if (exchangeRate != null) {
                return amount / exchangeRate.getRate();
            }
        }
        return null;
    }
}
