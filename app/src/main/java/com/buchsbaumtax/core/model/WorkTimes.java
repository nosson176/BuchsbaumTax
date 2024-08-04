package com.buchsbaumtax.core.model;

public class WorkTimes {
    private int id;
    private int userId;
    private long startTime; // Unix timestamp in milliseconds
    private long endTime;   // Unix timestamp in milliseconds
    private long sumHoursWork;
    private String username;
    private long date;      // Unix timestamp for date

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getSumHoursWork() {
        return sumHoursWork;
    }

    public void setSumHoursWork(long sumHoursWork) {
        this.sumHoursWork = sumHoursWork;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


}


