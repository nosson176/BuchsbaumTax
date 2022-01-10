package com.buchsbaumtax.core.model;


import java.util.Date;

public class SmartviewLine {
    private Integer id;
    private Date created;
    private Date updated;
    private long smartviewId;
    private int query;
    private String classToJoin;
    private String fieldToSearch;
    private String searchValue;
    private String operator;
    private String type;

    public SmartviewLine() {
    }

    public SmartviewLine(Integer id, Date created, Date updated, long smartviewId, int query, String classToJoin, String fieldToSearch, String searchValue, String operator, String type) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.smartviewId = smartviewId;
        this.query = query;
        this.classToJoin = classToJoin;
        this.fieldToSearch = fieldToSearch;
        this.searchValue = searchValue;
        this.operator = operator;
        this.type = type;
    }

    public Integer getId() {
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

    public void setSmartviewId(long smartviewId) {
        this.smartviewId = smartviewId;
    }

    public int getQuery() {
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
