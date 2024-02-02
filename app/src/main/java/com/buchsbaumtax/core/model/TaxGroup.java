package com.buchsbaumtax.core.model;

public class TaxGroup {

    private int id;
    private String key;
    private String value;
    private boolean show = true;
    private boolean include = true;
    private boolean selfEmployment;
    private boolean passive;
    private String subType;

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isShow() {
        return show;
    }

    public boolean isInclude() {
        return include;
    }

    public boolean isSelfEmployment() {
        return selfEmployment;
    }

    public boolean isPassive() {
        return passive;
    }

    public String getSubType() {
        return subType;
    }
}
