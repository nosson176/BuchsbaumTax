package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.dao.*;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class ClientData {
    public static final String DOLLARS = "USD";
    public static final String SHEKELS = "NIS";

    private int id;
    private String lastName;
    private String status;
    private String owesStatus;
    private boolean archived;
    private String periodical;
    private String displayName;
    private String displayPhone;
    private Date created;
    private List<TaxYearData> taxYearData;
    private List<FbarBreakdown> fbarBreakdowns;
    private List<IncomeBreakdown> incomeBreakdowns;
    private List<Log> logs;
    private List<Contact> contacts;
    private List<TaxPersonal> taxPersonals;
    private List<Fee> fees;
    private List<Checklist> checklists;
    private Double owesDollars;
    private Double paidDollars;
    private Double owesShekels;
    private Double paidShekels;
    private Double feesOwesDollars;
    private Double feesPaidDollars;
    private Double feesOwesShekels;
    private Double feesPaidShekels;


    public ClientData(Client client, List<TaxYearData> taxYearData) {
        this.id = client.getId();
        this.lastName = client.getLastName();
        this.status = client.getStatus();
        this.owesStatus = client.getOwesStatus();
        this.archived = client.isArchived();
        this.periodical = client.getPeriodical();
        this.displayName = client.getDisplayName();
        this.displayPhone = client.getDisplayPhone();
        this.created = client.getCreated();
        this.taxYearData = taxYearData;
        this.fbarBreakdowns = Database.dao(FbarBreakdownDAO.class).getForClient(client.getId());
        this.incomeBreakdowns = Database.dao(IncomeBreakdownDAO.class).getForClient(client.getId());
        this.logs = Database.dao(LogDAO.class).getForClient(client.getId());
        this.contacts = Database.dao(ContactDAO.class).getForClient(client.getId());
        this.taxPersonals = Database.dao(TaxPersonalDAO.class).getForClient(client.getId());
        this.fees = Database.dao(FeeDAO.class).getForClient(client.getId());
        this.checklists = Database.dao(ChecklistDAO.class).getForClient(client.getId());

        Stream<Filing> clientFilings = Database.dao(FilingDAO.class).getByClient(client.getId()).stream().filter(Filing::isIncludeInRefund);
        this.owesDollars = Database.dao(FilingDAO.class).getByClient(client.getId()).stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(f -> f.getOwes() + f.getOwesFee()).sum();
        this.paidDollars = Database.dao(FilingDAO.class).getByClient(client.getId()).stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(f -> f.getPaid() + f.getPaidFee()).sum();
        this.owesShekels = Database.dao(FilingDAO.class).getByClient(client.getId()).stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(SHEKELS))
                .mapToDouble(f -> f.getOwes() + f.getOwesFee()).sum();
        this.paidShekels = Database.dao(FilingDAO.class).getByClient(client.getId()).stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(SHEKELS))
                .mapToDouble(f -> f.getPaid() + f.getPaidFee()).sum();

        Stream<Fee> clientFees = Database.dao(FeeDAO.class).getForClient(client.getId()).stream().filter(Fee::isInclude);
        this.feesOwesDollars = Database.dao(FeeDAO.class).getForClient(client.getId()).stream()
                .filter(Fee::isInclude)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(Fee::getManualAmount).sum();
        this.feesPaidDollars = Database.dao(FeeDAO.class).getForClient(client.getId()).stream()
                .filter(Fee::isInclude)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(Fee::getPaidAmount).sum();
        this.feesOwesShekels = Database.dao(FeeDAO.class).getForClient(client.getId()).stream()
                .filter(Fee::isInclude)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(SHEKELS))
                .mapToDouble(Fee::getManualAmount).sum();
        this.feesPaidShekels = Database.dao(FeeDAO.class).getForClient(client.getId()).stream()
                .filter(Fee::isInclude)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(SHEKELS))
                .mapToDouble(Fee::getPaidAmount).sum();
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public String getOwesStatus() {
        return owesStatus;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getPeriodical() {
        return periodical;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public Date getCreated() {
        return created;
    }

    public List<TaxYearData> getTaxYears() {
        return taxYearData;
    }

    public List<FbarBreakdown> getFbarBreakdowns() {
        return fbarBreakdowns;
    }

    public List<IncomeBreakdown> getIncomeBreakdowns() {
        return incomeBreakdowns;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<TaxPersonal> getTaxPersonals() {
        return taxPersonals;
    }

    public List<Fee> getFees() {
        return fees;
    }

    public List<Checklist> getChecklists() {
        return checklists;
    }

    public Double getOwesDollars() {
        return owesDollars;
    }

    public Double getPaidDollars() {
        return paidDollars;
    }

    public Double getOwesShekels() {
        return owesShekels;
    }

    public Double getPaidShekels() {
        return paidShekels;
    }

    public Double getFeesOwesDollars() {
        return feesOwesDollars;
    }

    public Double getFeesPaidDollars() {
        return feesPaidDollars;
    }

    public Double getFeesOwesShekels() {
        return feesOwesShekels;
    }

    public Double getFeesPaidShekels() {
        return feesPaidShekels;
    }
}
