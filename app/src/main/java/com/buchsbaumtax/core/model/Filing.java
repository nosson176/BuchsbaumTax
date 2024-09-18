package com.buchsbaumtax.core.model;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Filing {
    public static final String FILING_TYPE_FEDERAL = "federal";
    public static final String FILING_TYPE_STATE = "state";
    static final Logger logger = LoggerFactory.getLogger(Filing.class);
    private int id;
    private String currency;
    private boolean completed;
    private int taxYearId;
    private Long dateFiled; // Keep as java.sql.Date if used for SQL
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
    private Long statusDate;
    private String fileType;
    private Status status;
    private Status statusDetail;
    private String taxForm;
    private int sortOrder;
    private double amount;
    private Integer clientId;

    // Default constructor
    public Filing() {
    }

    // Constructor with taxYearId and filingType
    public Filing(int taxYearId, String filingType) {
        this.taxYearId = taxYearId;
        this.filingType = filingType;
    }

    // Getters and Setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getTaxYearId() {
        return taxYearId;
    }

    public void setTaxYearId(int taxYearId) {
        this.taxYearId = taxYearId;
    }

    public Long getDateFiled() {
        return dateFiled;
    }

    public void setDateFiled(Long dateFiled) {
        this.dateFiled = dateFiled;
    }

    public String getSecondDeliveryContact() {
        return secondDeliveryContact;
    }

    public void setSecondDeliveryContact(String secondDeliveryContact) {
        this.secondDeliveryContact = secondDeliveryContact;
    }

    public String getDeliveryContact() {
        return deliveryContact;
    }

    public void setDeliveryContact(String deliveryContact) {
        this.deliveryContact = deliveryContact;
    }

    public double getRebate() {
        return rebate;
    }

    public void setRebate(double rebate) {
        this.rebate = rebate;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public boolean isIncludeFee() {
        return includeFee;
    }

    public void setIncludeFee(boolean includeFee) {
        this.includeFee = includeFee;
    }

    public boolean isIncludeInRefund() {
        return includeInRefund;
    }

    public void setIncludeInRefund(boolean includeInRefund) {
        this.includeInRefund = includeInRefund;
    }

    public double getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(double paidFee) {
        this.paidFee = paidFee;
    }

    public double getOwesFee() {
        return owesFee;
    }

    public void setOwesFee(double owesFee) {
        this.owesFee = owesFee;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getOwes() {
        return owes;
    }

    public void setOwes(double owes) {
        this.owes = owes;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFilingType() {
        return filingType;
    }

    public void setFilingType(String filingType) {
        this.filingType = filingType;
    }

    public Long getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Long statusDate) {
        this.statusDate = statusDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
//        logger.info("Retrieved statusJson from filings - ID: {}", status);
        this.status = status;
    }

    public Status getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(Status statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getTaxForm() {
        return taxForm;
    }

    public void setTaxForm(String taxForm) {
        this.taxForm = taxForm;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

//    public static Date localDateToSqlDate(java.util.Date localDate) {
//        return Date.valueOf(localDate);
//    }
//
//    public static LocalDate sqlDateToLocalDate(Date sqlDate) {
//        return sqlDate.toLocalDate();
//    }

    @Override
    public String toString() {
        return "Filing{" +
                "id=" + id +
                ", currency='" + (currency != null ? currency : "null") + '\'' +
                ", completed=" + completed +
                ", taxYearId=" + taxYearId +
                ", dateFiled=" + dateFiled +
                ", secondDeliveryContact='" + (secondDeliveryContact != null ? secondDeliveryContact : "null") + '\'' +
                ", deliveryContact='" + (deliveryContact != null ? deliveryContact : "null") + '\'' +
                ", rebate=" + rebate +
                ", refund=" + refund +
                ", includeFee=" + includeFee +
                ", includeInRefund=" + includeInRefund +
                ", paidFee=" + paidFee +
                ", owesFee=" + owesFee +
                ", paid=" + paid +
                ", owes=" + owes +
                ", memo='" + (memo != null ? memo : "null") + '\'' +
                ", state='" + (state != null ? state : "null") + '\'' +
                ", filingType='" + (filingType != null ? filingType : "null") + '\'' +
                ", statusDate=" + statusDate +
                ", fileType='" + (fileType != null ? fileType : "null") + '\'' +
                ", status=" + (status != null ? status.toString() : "null") +
                ", statusDetail=" + (statusDetail != null ? statusDetail.toString() : "null") +
                ", taxForm='" + (taxForm != null ? taxForm : "null") + '\'' +
                ", sortOrder=" + sortOrder +
                ", amount=" + amount +
                ", clientId=" + clientId +
                '}';
    }
}
