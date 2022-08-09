package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.ClientFlag;

import java.util.Date;

public class ClientInfo {

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
    private ClientFlag flag;

    public ClientInfo() {
    }

    public ClientInfo(Client client, int userId) {
        this.id = client.getId();
        this.lastName = client.getLastName();
        this.status = client.getStatus();
        this.owesStatus = client.getOwesStatus();
        this.archived = client.isArchived();
        this.periodical = client.getPeriodical();
        this.displayName = client.getDisplayName();
        this.displayPhone = client.getDisplayPhone();
        this.created = client.getCreated();
        this.updated = client.getUpdated();
        this.flag = client.getFlags().stream().filter(f -> f.getUserId() != null && f.getUserId() == userId).findFirst().orElse(null);
    }


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

    public ClientFlag getFlag() {
        return flag;
    }

    public void setFlag(ClientFlag flag) {
        this.flag = flag;
    }
}
