package com.buchsbaumtax.core.model;

import java.security.Principal;
import java.util.Date;

public class User implements Principal {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userType;
    private Date created;
    private Date updated;

    public int getId(){return id;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
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

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
