package com.buchsbaumtax.core.model;


import java.util.Date;

public class SmartviewLine {
    private int id;
    private Date created;
    private Date updated;
    private long smartviewId;
    private long query;
    private String classToJoin;
    private String fieldToSearch;
    private String searchValue;
    private String operator;
    private String type;

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public long getSmartviewId() {
        return smartviewId;
    }

    public long getQuery() {
        return query;
    }

    public String getClassToJoin() {
        return classToJoin;
    }

    public String getFieldToSearch() {
        return fieldToSearch;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public String getOperator() {
        return operator;
    }

    public String getType() {
        return type;
    }
}
