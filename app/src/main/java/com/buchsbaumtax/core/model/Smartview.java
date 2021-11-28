package com.buchsbaumtax.core.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Smartview {
    private Integer id;
    private String userName;
    private long userId;
    private String name;
    private long sortNumber;
    private boolean archived;
    private List<Integer> clientIds;
    private Date created;
    private Date updated;
    private List<SmartviewLine> smartviewLines = new ArrayList<>();

    public Smartview() {
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public long getSortNumber() {
        return sortNumber;
    }

    public boolean getArchived() {
        return archived;
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
