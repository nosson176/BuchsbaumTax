package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.model.Smartview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmartviewData {
    private Integer id;
    private String userName;
    private Integer userId;
    private String name;
    private int sortNumber;
    private boolean archived;
    private List<Integer> clientIds;
    private Date created;
    private Date updated;
    private List<SmartviewLineData> smartviewLines = new ArrayList<>();

    public SmartviewData() {
    }

    public SmartviewData(Smartview smartview, List<SmartviewLineData> smartviewLines) {
        this.id = smartview.getId();
        this.userName = smartview.getUserName();
        this.userId = smartview.getUserId();
        this.name = smartview.getName();
        this.sortNumber = smartview.getSortNumber();
        this.archived = smartview.isArchived();
        this.clientIds = smartview.getClientIds();
        this.created = smartview.getCreated();
        this.updated = smartview.getUpdated();
        this.smartviewLines = smartviewLines;
    }


    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public boolean isArchived() {
        return archived;
    }

    public List<Integer> getClientIds() {
        return clientIds;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public List<SmartviewLineData> getSmartviewLines() {
        return smartviewLines;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
