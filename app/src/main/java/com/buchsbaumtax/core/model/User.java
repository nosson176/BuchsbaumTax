package com.buchsbaumtax.core.model;

import java.security.Principal;
import java.util.Date;

public class User implements Principal {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private boolean sendLoginNotifications;
    private boolean notifyOfLogins;
    private Integer secondsInDay;
    private boolean allowTexting;
    private boolean selectable;
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
