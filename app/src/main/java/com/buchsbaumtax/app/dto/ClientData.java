package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.dao.*;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;

import java.util.Date;
import java.util.List;

public class ClientData {

    private int id;
    private String lastName;
    private String currentStatus;
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

    public ClientData(Client client, List<TaxYearData> taxYearData) {
        this.id = client.getId();
        this.lastName = client.getLastName();
        this.currentStatus = client.getStatus();
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
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCurrentStatus() {
        return currentStatus;
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
}
