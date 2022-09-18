package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class TaxYearData implements Comparable<TaxYearData> {
    private int id;
    private int clientId;
    private String year;
    private boolean archived;
    private boolean irsHistory;
    private List<Filing> filings;

    public TaxYearData() {
    }

    public TaxYearData(TaxYear taxYear) {
        this.id = taxYear.getId();
        this.clientId = taxYear.getClientId();
        this.year = taxYear.getYear();
        this.archived = taxYear.isArchived();
        this.irsHistory = taxYear.isIrsHistory();
        this.filings = Database.dao(FilingDAO.class).getByTaxYear(taxYear.getId());
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public String getYear() {
        return year;
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isIrsHistory() {
        return irsHistory;
    }

    public List<Filing> getFilings() {
        return filings;
    }

    @Override
    public int compareTo(TaxYearData ty) {
        if (getYear() == null) {
            return -1;
        }
        if (ty.getYear() == null) {
            return 1;
        }
        if (getYear().equals("DELETE") && !ty.getYear().equals("DELETE")) {
            return -1;
        }
        else if (!getYear().equals("DELETE") && ty.getYear().equals("DELETE")) {
            return 1;
        }
        return getYear().compareTo(ty.getYear());
    }
}
