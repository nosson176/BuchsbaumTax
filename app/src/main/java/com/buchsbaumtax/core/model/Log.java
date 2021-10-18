package com.buchsbaumtax.core.model;

import java.util.Date;

public class Log {
    private int id;
    private boolean alerted;
    private int priority;
    private String alarmTime;
    private int clientId;
    private int secondsSpent;
    private int alarmUserId;
    private boolean alarmComplete;
    private boolean alert;
    private Date alarmDate;
    private Date logDate;
    private String note;
    private boolean archived;
    private String years;

    public int getId() {
        return id;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public int getPriority() {
        return priority;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public int getClientId() {
        return clientId;
    }

    public int getSecondsSpent() {
        return secondsSpent;
    }

    public int getAlarmUserId() {
        return alarmUserId;
    }

    public boolean isAlarmComplete() {
        return alarmComplete;
    }

    public boolean isAlert() {
        return alert;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public Date getLogDate() {
        return logDate;
    }

    public String getNote() {
        return note;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getYears() {
        return years;
    }
}
