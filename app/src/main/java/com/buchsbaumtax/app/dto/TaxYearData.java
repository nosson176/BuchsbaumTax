package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class TaxYearData {
    private int id;
    private int yearDetailId;
    private boolean archived;
    private String yearName;
    private boolean irsHistory;
    private List<Filing> filings;

    public TaxYearData(TaxYear taxYear) {
        this.id = taxYear.getId();
        this.yearDetailId = taxYear.getYearDetailId();
        this.archived = taxYear.isArchived();
        this.yearName = taxYear.getYearName();
        this.irsHistory = taxYear.isIrsHistory();
        this.filings = Database.dao(FilingDAO.class).getByTaxYear(taxYear.getId());
    }

    public int getId() {
        return id;
    }

    public int getYearDetailId() {
        return yearDetailId;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getYearName() {
        return yearName;
    }

    public boolean isIrsHistory() {
        return irsHistory;
    }

    public List<Filing> getFilings() {
        return filings;
    }
}
