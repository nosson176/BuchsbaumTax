package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.model.Value;

public class ValueObject {

    private final int id;
    private final int sortOrder;
    private final String value;
    private final Integer parentId;
    private final boolean show;
    private final boolean include;

    public ValueObject(Value value) {
        this.id = value.getId();
        this.sortOrder = value.getSortOrder();
        this.value = value.getValue();
        this.parentId = value.getParentId();
        this.show = value.isShow();
        this.include = value.isInclude();
    }

    public int getId() {
        return id;
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
