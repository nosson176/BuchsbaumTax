package com.buchsbaumtax.core.model;


import java.util.Date;

public class TimeSlip {

    private int id;
    private int userId;
    private Date timeIn;
    private Date timeOut;
    private String memo;
    private Date created;
    private Date updated;


    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Date getTimeIn() {
        return timeIn;
    }

    public Date getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Date timeOut) {
        this.timeOut = timeOut;
    }

    public String getMemo() {
        return memo;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
