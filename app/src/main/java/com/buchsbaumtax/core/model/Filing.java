package com.buchsbaumtax.core.model;


import java.util.Date;

public class Filing {

    private String currency;
    private boolean completed;
    private long taxYearId;
    private Date dateFiled;
    private long secondDeliveryContactId;
    private long deliveryContactId;
    private double rebate;
    private double refund;
    private boolean includeFee;
    private boolean includeInRefund;
    private double paidFee;
    private double owesFee;
    private double paid;
    private double owes;
    private String memo;
    private String state;
    private String filingType;
    private Date statusDate;
    private String taxYear;
    private String fileType;
    private String statusDetail;
    private String status;
    private String taxForm;

    public String getCurrency() {
        return currency;
    }

    public boolean getCompleted() {
        return completed;
    }

    public long getTaxYearId() {
        return taxYearId;
    }

    public Date getDateFiled() {
        return dateFiled;
    }

    public long getSecondDeliveryContactId() {
        return secondDeliveryContactId;
    }

    public long getDeliveryContactId() {
        return deliveryContactId;
    }

    public double getRebate() {
        return rebate;
    }

    public double getRefund() {
        return refund;
    }

    public boolean getIncludeFee() {
        return includeFee;
    }

    public boolean getIncludeInRefund() {
        return includeInRefund;
    }

    public double getPaidFee() {
        return paidFee;
    }

    public double getOwesFee() {
        return owesFee;
    }

    public double getPaid() {
        return paid;
    }

    public double getOwes() {
        return owes;
    }

    public String getMemo() {
        return memo;
    }

    public String getState() {
        return state;
    }

    public String getFilingType() {
        return filingType;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public String getTaxYear() {
        return taxYear;
    }

    public String getFileType() {
        return fileType;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public String getStatus() {
        return status;
    }

    public String getTaxForm() {
        return taxForm;
    }
}
