package com.buchsbaumtax.core.model.create;

public class UserCreate {
    private String firstName;
    private String lastName;
    private String username;
    private String userType;
    private String password;
    private boolean sendLoginNotifications;
    private boolean notifyOfLogins;
    private Integer secondsInDay;
    private boolean allowTexting;
    private boolean selectable;

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
}
