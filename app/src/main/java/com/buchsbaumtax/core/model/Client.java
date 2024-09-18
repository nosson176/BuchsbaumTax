package com.buchsbaumtax.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client {

    private int id;
    private String lastName;
    private String status;
    private String owesStatus;
    private boolean archived;
    private String periodical;
    private String displayName;
    private String displayPhone;
    private Date created;
    private Date updated;
    private Long statusChangeDate;
    private List<ClientFlag> flags = new ArrayList<>();


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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Long getStatusChangeDate() {
        return statusChangeDate;
    }

    public Long setStatusChangeDate(Long date) {
        return statusChangeDate = date;
    }

    public List<ClientFlag> getFlags() {
        return flags;
    }

    public void setFlags(List<ClientFlag> flags) {
        this.flags = flags;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                ", owesStatus='" + owesStatus + '\'' +
                ", archived=" + archived +
                ", periodical='" + periodical + '\'' +
                ", displayName='" + displayName + '\'' +
                ", displayPhone='" + displayPhone + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", statusChangeDate=" + statusChangeDate +
                ", flags=" + flags +
                '}';
    }


}

