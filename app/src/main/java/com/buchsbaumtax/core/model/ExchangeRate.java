package com.buchsbaumtax.core.model;


public class ExchangeRate {
    private int id;
    private String currency;
    private String year;
    private boolean show;
    private double rate;

    public String getCurrency() {
        return currency;
    }

    public String getYear() {
        return year;
    }

    public boolean getShow() {
        return show;
    }

    public double getRate() {
        return rate;
    }
}
