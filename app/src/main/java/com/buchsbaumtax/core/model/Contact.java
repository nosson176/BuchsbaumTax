package com.buchsbaumtax.core.model;

public class Contact {
    private boolean archived;
    private int zip;
    private String state;
    private String secondaryDetail;
    private String mainDetail;
    private String memo;
    private String contactType;
    private boolean disabled;
    private int clientId;

    public boolean isArchived() {
        return archived;
    }

    public int getZip() {
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

    public boolean isDisabled() {
        return disabled;
    }

    public int getClientId() {
        return clientId;
    }
}
