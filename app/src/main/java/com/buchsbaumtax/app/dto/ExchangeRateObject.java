package com.buchsbaumtax.app.dto;

public class ExchangeRateObject {
    private String year;
    private String currency;

    public ExchangeRateObject(String year, String currency) {
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final ExchangeRateObject object = (ExchangeRateObject)obj;
        return (object.getYear() + object.getCurrency()).equals(this.getYear() + this.getCurrency());
    }
}
