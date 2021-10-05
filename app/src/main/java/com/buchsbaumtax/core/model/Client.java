package com.buchsbaumtax.core.model;

public class Client {

    private int id;
    private String lastName;
    private String status;
    private String owesStatus;
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

    public String getStatus() {
        return status;
    }

    public String getOwesStatus() {
        return owesStatus;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }
}
