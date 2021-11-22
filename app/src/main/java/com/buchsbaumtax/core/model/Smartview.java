package com.buchsbaumtax.core.model;


import java.util.Date;
import java.util.List;

public class Smartview {
    private int id;
    private String userName;
    private long userId;
    private String name;
    private long sortNumber;
    private boolean archived;
    private Integer clientCount;
    private List<Integer> clientIds;
    private Date created;
    private Date updated;

    public int getId() {
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

    public boolean getArchived() {
        return archived;
    }

    public Integer getClientCount() {
        return clientCount;
    }

    public void setClientCount(Integer clientCount) {
        this.clientCount = clientCount;
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
}
