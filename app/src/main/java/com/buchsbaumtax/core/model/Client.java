package com.buchsbaumtax.core.model;

public class Client {

    private int id;
    private String lastName;
    private String currentStatus;
    private boolean archived;
    private String periodical;
    private String displayName;
    private String displayPhone;

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getPeriodical() {
        return periodical;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }
}
