package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class ConvertToUSD {
    private static final List<ExchangeRate> exchangeRates = Database.dao(ExchangeRateDAO.class).getAll();

    public static Double convertToUSD(double amount, String currency, String year) {
        if (currency != null && year != null) {
            if (currency.equals("USD")) {
                return amount;
            }
            ExchangeRate exchangeRate = exchangeRates.stream().filter(er -> (er.getYear() != null && er.getYear().equals(year)) && (er.getCurrency() != null && er.getCurrency().equals(currency))).findFirst().orElse(null);
            if (exchangeRate != null) {
                return amount / exchangeRate.getRate();
            }
        }
        return null;
    }
}
