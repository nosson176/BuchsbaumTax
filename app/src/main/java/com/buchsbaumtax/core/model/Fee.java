package com.buchsbaumtax.core.model;

import java.util.Date;

public class Fee {
    private int id;
    private int clientId;
    private String year;
    private String status;
    private String statusDetail;
    private String feeType;
    private double manualAmount;
    private double paidAmount;
    private boolean include;
    private double rate;
    private Date dateFee;
    private boolean sum;
    private boolean archived;
    private String notes;


    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public String getYear() {
        return year;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public String getFeeType() {
        return feeType;
    }

    public double getManualAmount() {
        return manualAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getRate() {
        return rate;
    }

    public Date getDateFee() {
        return dateFee;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isInclude() {
        return include;
    }

    public boolean isSum() {
        return sum;
    }

    public boolean isArchived() {
        return archived;
    }
}
