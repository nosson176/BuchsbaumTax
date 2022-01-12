package com.buchsbaumtax.core.model;


import com.buchsbaumtax.app.dto.SmartviewLineData;
import com.buchsbaumtax.app.dto.SmartviewLineField;

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

    public SmartviewLine(SmartviewLineData smartviewLineData, SmartviewLineField fieldData, String searchValue) {
        this.id = smartviewLineData.getId();
        this.created = smartviewLineData.getCreated();
        this.updated = smartviewLineData.getUpdated();
        this.smartviewId = smartviewLineData.getSmartviewId();
        this.groupNum = smartviewLineData.getGroupNum();
        this.tableName = fieldData.getTableName();
        this.field = fieldData.getFieldName();
        this.searchValue = searchValue;
        this.operator = smartviewLineData.getOperator();
        this.type = fieldData.getType();
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
