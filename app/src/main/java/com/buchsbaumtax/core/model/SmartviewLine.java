package com.buchsbaumtax.core.model;


import com.buchsbaumtax.app.dto.SmartviewLineData;

import java.util.Date;
import java.util.Map;

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

    public SmartviewLine(SmartviewLineData smartviewLineData, Map<String, String> values) {
        this.id = smartviewLineData.getId();
        this.created = smartviewLineData.getCreated();
        this.updated = smartviewLineData.getUpdated();
        this.smartviewId = smartviewLineData.getSmartviewId();
        this.groupNum = smartviewLineData.getGroupNum();
        this.tableName = values.get("table");
        this.field = values.get("field");
        this.searchValue = values.get("searchValue");
        this.operator = smartviewLineData.getOperator();
        this.type = values.get("type");
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
