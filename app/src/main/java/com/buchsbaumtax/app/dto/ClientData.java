package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.dao.*;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ClientData {

    public static final String DOLLARS = "USD";
    public static final String SHEKELS = "NIS";

    private final int id;
    private final String lastName;
    private final String status;
    private final String owesStatus;
    private final boolean archived;
    private final String periodical;
    private final String displayName;
    private final String displayPhone;
    private final Date created;
    private final List<TaxYearData> taxYearData;
    private final List<FbarBreakdown> fbarBreakdowns;
    private final List<IncomeBreakdown> incomeBreakdowns;
    private final List<Log> logs;
    private final List<Contact> contacts;
    private final List<TaxPersonal> taxPersonals;
    private final List<Fee> fees;
    private final List<Checklist> checklists;
    private int flag;
    private final double owesDollars;
    private final double paidDollars;
    private final double owesShekels;
    private final double paidShekels;
    private final double feesOwesDollars;
    private final double feesPaidDollars;
    private final double feesOwesShekels;
    private final double feesPaidShekels;

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

        List<Filing> filings = taxYearData.stream().flatMap(t -> t.getFilings().stream()).collect(Collectors.toList());
        this.owesDollars = filings.stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(f -> f.getOwes() + f.getOwesFee()).sum();
        this.paidDollars = filings.stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(f -> f.getPaid() + f.getPaidFee()).sum();
        this.owesShekels = filings.stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(SHEKELS))
                .mapToDouble(f -> f.getOwes() + f.getOwesFee()).sum();
        this.paidShekels = filings.stream()
                .filter(Filing::isIncludeInRefund)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(SHEKELS))
                .mapToDouble(f -> f.getPaid() + f.getPaidFee()).sum();

        this.feesOwesDollars =fees.stream()
                .filter(Fee::isInclude)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(Fee::getManualAmount).sum();
        this.feesPaidDollars = fees.stream()
                .filter(Fee::isInclude)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(DOLLARS))
                .mapToDouble(Fee::getPaidAmount).sum();
        this.feesOwesShekels = fees.stream()
                .filter(Fee::isInclude)
                .filter(f -> f.getCurrency() != null && f.getCurrency().equals(SHEKELS))
                .mapToDouble(Fee::getManualAmount).sum();
        this.feesPaidShekels = fees.stream()
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getOwesDollars() {
        return owesDollars;
    }

    public double getPaidDollars() {
        return paidDollars;
    }

    public double getOwesShekels() {
        return owesShekels;
    }

    public double getPaidShekels() {
        return paidShekels;
    }

    public double getFeesOwesDollars() {
        return feesOwesDollars;
    }

    public double getFeesPaidDollars() {
        return feesPaidDollars;
    }

    public double getFeesOwesShekels() {
        return feesOwesShekels;
    }

    public double getFeesPaidShekels() {
        return feesPaidShekels;
    }
}
