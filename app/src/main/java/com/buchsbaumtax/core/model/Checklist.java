package com.buchsbaumtax.core.model;


public class Checklist {

    private int id;
    private boolean archived;
    private int clientId;
    private boolean finished;
    private String memo;
    private Integer taxYearId;
    private boolean translated;
    private Integer sortNumber;


    public int getId() {
        return id;
    }

    public boolean isArchived() {
        return archived;
    }

    public int getClientId() {
        return clientId;
    }

    public String getMemo() {
        return memo;
    }

    public Integer getTaxYearId() {
        return taxYearId;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isTranslated() {
        return translated;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }
}
