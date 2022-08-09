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

    public boolean getArchived() {
        return archived;
    }

    public int getClientId() {
        return clientId;
    }

    public boolean getFinished() {
        return finished;
    }

    public String getMemo() {
        return memo;
    }

    public Integer getTaxYearId() {
        return taxYearId;
    }

    public boolean getTranslated() {
        return translated;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }
}
