package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.dao.FbarBreakdownDAO;
import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class TaxYearData {
    private final int id;
    private final int yearDetailId;
    private final boolean archived;
    private final String yearName;
    private final boolean irsHistory;
    private final List<IncomeBreakdown> incomeBreakdowns;
    private final List<FbarBreakdown> fbarBreakdowns;

    public TaxYearData(TaxYear taxYear) {
        this.id = taxYear.getId();
        this.yearDetailId = taxYear.getYearDetailId();
        this.archived = taxYear.isArchived();
        this.yearName = taxYear.getYearName();
        this.irsHistory = taxYear.isIrsHistory();
        this.incomeBreakdowns = Database.dao(IncomeBreakdownDAO.class).getForTaxYear(taxYear.getId());
        this.fbarBreakdowns = Database.dao(FbarBreakdownDAO.class).getForTaxYear(taxYear.getId());
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

    public List<IncomeBreakdown> getIncomeBreakdowns() {
        return incomeBreakdowns;
    }

    public List<FbarBreakdown> getFbarBreakdowns() {
        return fbarBreakdowns;
    }
}
