package com.buchsbaumtax.core.model;

public class Value {
    private int id;
    private int sortOrder;
    private String key;
    private String value;
    private int ParentId;
    private boolean translationNeeded;
    private boolean passive;
    private boolean selfEmployment;
    private boolean show;
    private String subType;
    private boolean include;

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

    public int getParentId() {
        return ParentId;
    }

    public boolean isTranslationNeeded() {
        return translationNeeded;
    }

    public boolean isPassive() {
        return passive;
    }

    public boolean isSelfEmployment() {
        return selfEmployment;
    }

    public boolean isShow() {
        return show;
    }

    public String getSubType() {
        return subType;
    }

    public boolean isInclude() {
        return include;
    }
}
