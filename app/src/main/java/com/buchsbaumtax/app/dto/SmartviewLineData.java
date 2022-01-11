package com.buchsbaumtax.app.dto;

import java.util.Date;

public class SmartviewLineData {
    private int id;
    private Date created;
    private Date updated;
    private long smartviewId;
    private int groupNum;
    private String fieldName;
    private String searchValue;
    private String operator;

    public SmartviewLineData() {
    }

    public SmartviewLineData(int id, Date created, Date updated, long smartviewId, int groupNum, String fieldName, String searchValue, String operator) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.smartviewId = smartviewId;
        this.groupNum = groupNum;
        this.fieldName = fieldName;
        this.searchValue = searchValue;
        this.operator = operator;
    }


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

    public int getGroupNum() {
        return groupNum;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public String getOperator() {
        return operator;
    }
}
