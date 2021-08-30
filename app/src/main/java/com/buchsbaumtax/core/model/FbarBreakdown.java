package com.buchsbaumtax.core.model;

public class FbarBreakdown {

    private boolean include;
    private String depend;
    private String description;
    private String documents;
    private int frequency;
    private int currencyId;
    private int amount;
    private int partId;
    private int taxTypeId;
    private int taxGroupId;
    private int categoryId;
    private boolean archived;
    private int clientId;

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

    public int getCurrencyId() {
        return currencyId;
    }

    public int getAmount() {
        return amount;
    }

    public int getPartId() {
        return partId;
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

    public int getClientId() {
        return clientId;
    }
}
