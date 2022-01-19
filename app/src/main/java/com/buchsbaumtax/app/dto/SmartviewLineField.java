package com.buchsbaumtax.app.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartviewLineField that = (SmartviewLineField)o;
        return Objects.equals(tableName, that.tableName) && Objects.equals(fieldName, that.fieldName) && Objects.equals(type, that.type) && Objects.equals(tableName2, that.tableName2) && Objects.equals(fieldName2, that.fieldName2) && Objects.equals(searchValue, that.searchValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, fieldName, type, tableName2, fieldName2, searchValue);
    }
}
