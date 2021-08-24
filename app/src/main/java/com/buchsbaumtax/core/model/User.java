package com.buchsbaumtax.core.model;

import java.security.Principal;
import java.util.Date;

public class User implements Principal {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String userType;
    private Date created;
    private Date updated;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
