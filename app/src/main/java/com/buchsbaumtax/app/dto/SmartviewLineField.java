package com.buchsbaumtax.app.dto;

public class SmartviewLineField {
    private String tableName;
    private String fieldName;
    private String type;
    private String tableName2;
    private String fieldName2;
    private String searchValue;

    public SmartviewLineField(String tableName, String fieldName, String type) {
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.type = type;
    }

    public SmartviewLineField(String tableName, String fieldName, String type, String tableName2, String fieldName2, String searchValue) {
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.type = type;
        this.tableName2 = tableName2;
        this.fieldName2 = fieldName2;
        this.searchValue = searchValue;
    }

    public String getTableName() {
        return tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getType() {
        return type;
    }

    public String getTableName2() {
        return tableName2;
    }

    public String getFieldName2() {
        return fieldName2;
    }

    public String getSearchValue() {
        return searchValue;
    }
}
