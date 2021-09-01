package com.buchsbaumtax.core.model;

public class IncomeBreakdown {

    private String depend;
    private boolean include;
    private int clientId;
    private boolean exclusion;
    private double amount;
    private String description;
    private String documents;
    private int frequency;
    private int currencyId;
    private int jobId;
    private int taxTypeId;
    private int taxGroupId;
    private int categoryId;
    private boolean archived;

    public String getDepend() {
        return depend;
    }

    public boolean isInclude() {
        return include;
    }

    public int getClientId() {
        return clientId;
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

    public int getCurrencyId() {
        return currencyId;
    }

    public int getJobId() {
        return jobId;
    }

    public int getTaxTypeId() {
        return taxTypeId;
    }

    public int getTaxGroupId() {
        return taxGroupId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public boolean isArchived() {
        return archived;
    }
}
