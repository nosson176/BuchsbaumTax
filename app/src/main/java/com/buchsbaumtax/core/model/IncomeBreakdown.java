package com.buchsbaumtax.core.model;

public class IncomeBreakdown {

    private String depend;
    private boolean include;
    private boolean exclusion;
    private double amount;
    private String description;
    private String documents;
    private int frequency;
    private boolean archived;

    public String getDepend() {
        return depend;
    }

    public boolean isInclude() {
        return include;
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

    public boolean isArchived() {
        return archived;
    }
}
