package com.buchsbaumtax.core.model;

public class IncomeBreakdown {
    private int id;
    private String depend;
    private boolean include;
    private int clientId;
    private String years;
    private boolean exclusion;
    private double amount;
    private String description;
    private String documents;
    private int frequency;
    private String currency;
    private String job;
    private String taxType;
    private String taxGroup;
    private String category;
    private boolean archived;

    public int getId() {
        return id;
    }

    public String getDepend() {
        return depend;
    }

    public boolean isInclude() {
        return include;
    }

    public int getClientId() {
        return clientId;
    }

    public String getYears() {
        return years;
    }

    public boolean isExclusion() {
        return exclusion;
    }

    public double getAmount() {
        return amount;
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

    public String getJob() {
        return job;
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
}
