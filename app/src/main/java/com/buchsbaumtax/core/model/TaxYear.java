package com.buchsbaumtax.core.model;

public class TaxYear {

    private int id;
    private int clientId;
    private String year;
    private boolean archived;
    private boolean show;
    private boolean irsHistory;

    public TaxYear() {
    }

    public TaxYear(int clientId, String year, boolean archived, boolean irsHistory) {
        this.clientId = clientId;
        this.year = year;
        this.archived = archived;
        this.irsHistory = irsHistory;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public String getYear() {
        return year;
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isShow() {
        return show;
    }

    public boolean isIrsHistory() {
        return irsHistory;
    }
}
