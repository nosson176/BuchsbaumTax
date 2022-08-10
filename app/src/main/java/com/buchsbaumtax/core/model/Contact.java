package com.buchsbaumtax.core.model;

public class Contact {
    private int id;
    private boolean archived;
    private String zip;
    private String state;
    private String secondaryDetail;
    private String mainDetail;
    private String memo;
    private String contactType;
    private boolean enabled;
    private int clientId;
    private int sortOrder;

    public int getId() {
        return id;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getZip() {
        return zip;
    }

    public String getState() {
        return state;
    }

    public String getSecondaryDetail() {
        return secondaryDetail;
    }

    public String getMainDetail() {
        return mainDetail;
    }

    public String getMemo() {
        return memo;
    }

    public String getContactType() {
        return contactType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getClientId() {
        return clientId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
