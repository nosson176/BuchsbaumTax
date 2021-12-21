package com.buchsbaumtax.core.model;


public class Checklist {

    private long id;
    private boolean archived;
    private long clientId;
    private boolean finished;
    private String memo;
    private Long taxYearId;
    private boolean translated;
    private Long sortNumber;


    public long getId() {
        return id;
    }

    public boolean getArchived() {
        return archived;
    }

    public long getClientId() {
        return clientId;
    }

    public boolean getFinished() {
        return finished;
    }

    public String getMemo() {
        return memo;
    }

    public Long getTaxYearId() {
        return taxYearId;
    }

    public boolean getTranslated() {
        return translated;
    }

    public Long getSortNumber() {
        return sortNumber;
    }
}
