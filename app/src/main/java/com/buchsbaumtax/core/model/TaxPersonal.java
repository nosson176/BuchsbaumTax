package com.buchsbaumtax.core.model;

import java.util.Date;

public class TaxPersonal {
    private boolean archived;
    private int clientId;
    private int categoryId;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private Date dateOfBirth;
    private String ssn;
    private String informal;
    private int relationId;
    private int languageId;
    private boolean include;

    public boolean isArchived() {
        return archived;
    }

    public int getClientId() {
        return clientId;
    }

    public int getCategoryId() {
        return categoryId;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public String getInformal() {
        return informal;
    }

    public int getRelationId() {
        return relationId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public boolean isInclude() {
        return include;
    }
}
