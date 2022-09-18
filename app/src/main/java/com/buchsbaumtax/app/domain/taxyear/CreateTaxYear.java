package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.app.domain.FilingCRUD;
import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

public class CreateTaxYear {

    public TaxYearData createTaxYear(TaxYearData taxYear) {
        TaxYear year = new TaxYear(taxYear.getClientId(), taxYear.getYear(), taxYear.isArchived(), taxYear.isIrsHistory());
        int id = Database.dao(TaxYearDAO.class).create(year);

        if (taxYear.getFilings() != null && !taxYear.getFilings().isEmpty()) {
            FilingCRUD filingCRUD = new FilingCRUD();
            for (Filing filing : taxYear.getFilings()) {
                filing.setTaxYearId(id);
                filingCRUD.create(filing);
            }
        }
        else {
            Filing filing = new Filing(id, Filing.FILING_TYPE_FEDERAL);
            Database.dao(FilingDAO.class).create(filing);
        }

        return new TaxYearData(Database.dao(TaxYearDAO.class).get(id));
    }
}
