package com.buchsbaumtax.core.model;


public class Fee {
    private long clientId;
    private String year;
    private String status;
    private String statusDetail;
    private String feeType;
    private double manualAmount;
    private double paidAmount;
    private String include;
    private double rate;
    private java.util.Date dateFee;
    private String sum;
    private String archived;

    public long getClientId() {
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

    public String getInclude() {
        return include;
    }

    public double getRate() {
        return rate;
    }

    public java.util.Date getDateFee() {
        return dateFee;
    }

    public String getSum() {
        return sum;
    }

    public String getArchived() {
        return archived;
    }
}
