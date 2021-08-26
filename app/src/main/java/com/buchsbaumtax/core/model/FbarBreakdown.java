package com.buchsbaumtax.core.model;

public class FbarBreakdown {

    private boolean include;
    private String depend;
    private String description;
    private String documents;
    private int frequency;
    private int amount;
    private boolean archived;

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

    public int getAmount() {
        return amount;
    }

    public boolean isArchived() {
        return archived;
    }
}
