package com.buchsbaumtax.core.model;


import com.buchsbaumtax.app.dto.SmartviewData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Smartview {
    private Integer id;
    private String userName;
    private int userId;
    private String name;
    private int sortNumber;
    private boolean archived;
    private List<Integer> clientIds;
    private Date created;
    private Date updated;
    private List<SmartviewLine> smartviewLines = new ArrayList<>();

    public Smartview() {
    }

    public Smartview(SmartviewData smartviewData, List<SmartviewLine> smartviewLines) {
        this.id = smartviewData.getId();
        this.userName = smartviewData.getUserName();
        this.userId = smartviewData.getUserId();
        this.name = smartviewData.getName();
        this.sortNumber = smartviewData.getSortNumber();
        this.archived = smartviewData.isArchived();
        this.clientIds = smartviewData.getClientIds();
        this.created = smartviewData.getCreated();
        this.updated = smartviewData.getUpdated();
        this.smartviewLines = smartviewLines;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(int sortNumber) {
        this.sortNumber = sortNumber;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public List<Integer> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Integer> clientIds) {
        this.clientIds = clientIds;
    }

    public boolean isArchived() {
        return archived;
    }

    public List<SmartviewLine> getSmartviewLines() {
        return smartviewLines;
    }
}
