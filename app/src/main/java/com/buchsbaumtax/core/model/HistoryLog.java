package com.buchsbaumtax.core.model;

public class HistoryLog {
    private int id;              // Unique identifier for the history log
    private String userName;      // User who made the changes
    private long date;            // Date of the change in Unix time (milliseconds)
    private String field;         // The field that was changed
    private int logId;
    private String val;

    public HistoryLog() {}

    // Constructor
    public HistoryLog(int id, String userName, long date, String field, int logId) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.field = field;
        this.logId = logId;
        this.val = val;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
    public String getVal() {
        return val;
    }

    public void setVal(String val ) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "HistoryLog{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", date=" + date +
                ", field='" + field + '\'' +
                ", logId=" + logId +
                '}';
    }
}

