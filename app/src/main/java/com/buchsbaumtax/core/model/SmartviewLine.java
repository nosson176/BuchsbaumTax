package com.buchsbaumtax.core.model;


import java.util.Date;

public class SmartviewLine {
    private Integer id;
    private Date created;
    private Date updated;
    private long smartviewId;
    private int groupNum;
    private String tableName;
    private String field;
    private String searchValue;
    private String operator;
    private String type;

    public SmartviewLine() {
    }

    public SmartviewLine(Integer id, Date created, Date updated, long smartviewId, int groupNum, String tableName, String field, String searchValue, String operator, String type) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.smartviewId = smartviewId;
        this.groupNum = groupNum;
        this.tableName = tableName;
        this.field = field;
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

    public int getGroupNum() {
        return groupNum;
    }

    public String getTableName() {
        return tableName;
    }

    public String getField() {
        return field;
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
