package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.model.SmartviewLine;

import java.util.Date;

public class SmartviewLineData {
    private int id;
    private Date created;
    private Date updated;
    private int smartviewId;
    private int groupNum;
    private String fieldName;
    private String searchValue;
    private String operator;

    public SmartviewLineData() {
    }

    public SmartviewLineData(SmartviewLine smartviewLine, String fieldName) {
        this.id = smartviewLine.getId();
        this.created = smartviewLine.getCreated();
        this.updated = smartviewLine.getUpdated();
        this.smartviewId = smartviewLine.getSmartviewId();
        this.groupNum = smartviewLine.getGroupNum();
        this.fieldName = fieldName;
        this.searchValue = smartviewLine.getSearchValue();
        this.operator = smartviewLine.getOperator();
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

    public int getSmartviewId() {
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
