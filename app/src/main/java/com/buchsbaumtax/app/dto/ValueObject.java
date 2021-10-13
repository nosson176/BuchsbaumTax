package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.model.Value;

public class ValueObject {
    private int sortOrder;
    private String value;
    private int parentId;
    private boolean show;
    private boolean include;

    public ValueObject(Value value) {
        this.sortOrder = value.getSortOrder();
        this.value = value.getValue();
        this.parentId = value.getParentId();
        this.show = value.isShow();
        this.include = value.isInclude();
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public String getValue() {
        return value;
    }

    public int getParentId() {
        return parentId;
    }

    public boolean isShow() {
        return show;
    }

    public boolean isInclude() {
        return include;
    }
}
