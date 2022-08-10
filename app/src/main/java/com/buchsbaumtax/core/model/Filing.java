package com.buchsbaumtax.core.model;


import java.util.Date;

public class Filing {
    public static final String FILING_TYPE_FEDERAL = "federal";
    public static final String FILING_TYPE_STATE = "state";

    private int id;
    private String currency;
    private boolean completed;
    private int taxYearId;
    private Date dateFiled;
    private String secondDeliveryContact;
    private String deliveryContact;
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
    private String fileType;
    private String statusDetail;
    private String status;
    private String taxForm;
    private int sortOrder;
    private double amount;
    private Integer clientId;

    public Filing() {
    }

    public Filing(int taxYearId, String filingType) {
        this.taxYearId = taxYearId;
        this.filingType = filingType;
    }

    public int getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public int getTaxYearId() {
        return taxYearId;
    }

    public Date getDateFiled() {
        return dateFiled;
    }

    public String getSecondDeliveryContact() {
        return secondDeliveryContact;
    }

    public String getDeliveryContact() {
        return deliveryContact;
    }

    public double getRebate() {
        return rebate;
    }

    public double getRefund() {
        return refund;
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

    public boolean isCompleted() {
        return completed;
    }

    public boolean isIncludeFee() {
        return includeFee;
    }

    public boolean isIncludeInRefund() {
        return includeInRefund;
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

    public int getSortOrder() {
        return sortOrder;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
