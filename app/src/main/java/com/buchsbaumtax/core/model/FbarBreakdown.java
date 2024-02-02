package com.buchsbaumtax.core.model;

public class FbarBreakdown {
    private int id;
    private boolean include;
    private String depend;
    private String description;
    private String documents;
    private int frequency;
    private String currency;
    private int amount;
    private String part;
    private String taxType;
    private String taxGroup;
    private String category;
    private boolean archived;
    private int clientId;
    private String years;
    private Double amountUSD;

    public int getId() {
        return id;
    }

    public boolean isInclude() {
        return include;
    }

    public String getDepend() {
        return depend;
    }

    public String getDescription() {
        return description;
    }

    public String getDocuments() {
        return documents;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAmount() {
        return amount;
    }

    public String getPart() {
        return part;
    }

    public String getTaxType() {
        return taxType;
    }

    public String getTaxGroup() {
        return taxGroup;
    }

    public String getCategory() {
        return category;
    }

    public boolean isArchived() {
        return archived;
    }

    public int getClientId() {
        return clientId;
    }

    public String getYears() {
        return years;
    }

    public Double getAmountUSD() {
        return amountUSD;
    }

    public void setAmountUSD(Double amountUSD) {
        this.amountUSD = amountUSD;
    }
}
