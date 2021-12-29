package com.buchsbaumtax.core.model;

import java.util.Date;

public class Fee {
    private long id;
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


    public long getId() {
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

    public boolean getInclude() {
        return include;
    }

    public double getRate() {
        return rate;
    }

    public java.util.Date getDateFee() {
        return dateFee;
    }

    public boolean getSum() {
        return sum;
    }

    public boolean getArchived() {
        return archived;
    }
}
