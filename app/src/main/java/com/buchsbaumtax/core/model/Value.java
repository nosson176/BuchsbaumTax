package com.buchsbaumtax.core.model;

public class Value {
    private int id;
    private int sortOrder;
    private String key;
    private String value;
    private Integer parentId;
    private boolean translationNeeded;
    private boolean show = true;
    private boolean include = true;

    public int getId() {
        return id;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Integer getParentId() {
        return parentId;
    }

    public boolean isTranslationNeeded() {
        return translationNeeded;
    }

    public boolean isShow() {
        return show;
    }

    public boolean isInclude() {
        return include;
    }
}
