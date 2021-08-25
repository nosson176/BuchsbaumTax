package com.buchsbaumtax.core.model;

public class TaxYear {
    private int id;
    private int yearDetailId;
    private boolean archived;
    private String yearName;
    private boolean irsHistory;

    public int getId() {
        return id;
    }

    public int getYearDetailId() {
        return yearDetailId;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getYearName() {
        return yearName;
    }

    public boolean isIrsHistory() {
        return irsHistory;
    }
}
