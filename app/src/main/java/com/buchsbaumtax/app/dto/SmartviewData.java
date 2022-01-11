package com.buchsbaumtax.app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmartviewData {
    private Integer id;
    private String userName;
    private long userId;
    private String name;
    private long sortNumber;
    private boolean archived;
    private List<Integer> clientIds;
    private Date created;
    private Date updated;
    private List<SmartviewLineData> smartviewLines = new ArrayList<>();

    public SmartviewData() {
    }

    public SmartviewData(Integer id, String userName, long userId, String name, long sortNumber, boolean archived, List<Integer> clientIds, Date created, Date updated, List<SmartviewLineData> smartviewLines) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.name = name;
        this.sortNumber = sortNumber;
        this.archived = archived;
        this.clientIds = clientIds;
        this.created = created;
        this.updated = updated;
        this.smartviewLines = smartviewLines;
    }


    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public long getSortNumber() {
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

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
