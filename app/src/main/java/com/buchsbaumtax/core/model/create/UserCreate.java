package com.buchsbaumtax.core.model.create;

public class UserCreate {
    private String firstName;
    private String lastName;
    private String username;
    private String userType;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
