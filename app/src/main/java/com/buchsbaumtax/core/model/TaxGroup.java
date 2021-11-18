package com.buchsbaumtax.core.model;

public class TaxGroup {
    private String key;
    private String value;
    private boolean show = true;
    private boolean include = true;
    private boolean selfEmployment;
    private boolean passive;
    private String subType;

    public static final String EARNED_INCOME = "EARNED INCOME";
    public static final String PASSIVE_INCOME = "PASSIVE INCOME";
    public static final String TAXES_PAID = "TAXES PAID";
    public static final String DEPENDANT_EXPENSES = "DEP. EXPENSES";
    public static final String DEDUCTIONS = "DEDUCTIONS";
    public static final String SELF_EMPLOYMENT_INCOME = "SE INCOME";
    public static final String FBAR_YITROT = "FBAR YITROT";
    public static final String SELF_EMPLOYMENT_TAXES = "SE TAXES PAID";
    public static final String PASSIVE_TAXES = "PASS. TAXES PAID";

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
