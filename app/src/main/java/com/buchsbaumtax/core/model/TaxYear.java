package com.buchsbaumtax.core.model;

public class TaxYear {
    private int id;
    private int clientId;
    private String year;
    private boolean archived;
    private boolean irsHistory;

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

    public boolean isIrsHistory() {
        return irsHistory;
    }
}
