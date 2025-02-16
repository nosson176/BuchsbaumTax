package com.buchsbaumtax.core.model;

import java.util.Date;

public class TaxPersonal {
    public static final String CATEGORY_PRIMARY = "PRI.";
    public static final String CATEGORY_SECONDARY = "SEC.";
    public static final String CATEGORY_DEPENDANT = "DEP.";

    private int id;
    private boolean archived;
    private int clientId;
    private String category;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private Long dateOfBirth;
    private String ssn;
    private String informal;
    private String relation;
    private String language;
    private boolean include;

    public int getId() {
        return id;
    }

    public boolean isArchived() {
        return archived;
    }

    public int getClientId() {
        return clientId;
    }

    public String getCategory() {
        return category;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public String getInformal() {
        return informal;
    }

    public String getRelation() {
        return relation;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isInclude() {
        return include;
    }
}
