package com.buchsbaumtax.core.model;

import java.security.Principal;
import java.util.Date;

public class User implements Principal {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private boolean sendLoginNotifications;
    private boolean notifyOfLogins;
    private Integer secondsInDay;
    private boolean allowTexting;
    private boolean selectable;
    private String userType;
    private Date created;
    private Date updated;
    private  String email;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isSendLoginNotifications() {
        return sendLoginNotifications;
    }

    public boolean isNotifyOfLogins() {
        return notifyOfLogins;
    }

    public Integer getSecondsInDay() {
        return secondsInDay;
    }

    public boolean isAllowTexting() {
        return allowTexting;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public String getUserType() {
        return userType;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public String getName() {
        return username;
    }
}
