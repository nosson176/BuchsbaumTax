package com.buchsbaumtax.core.model;


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

    public Smartview() {
    }

    public Smartview(Integer id, String userName, long userId, String name, long sortNumber, boolean archived, Date created, Date updated) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.name = name;
        this.sortNumber = sortNumber;
        this.archived = archived;
        this.created = created;
        this.updated = updated;
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
}
