package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

public class CreateTaxYear {

    public TaxYear createTaxYear(TaxYear taxYear) {
        int id = Database.dao(TaxYearDAO.class).create(taxYear);
        Filing filing = new Filing(id, Filing.FILING_TYPE_FEDERAL);
        Database.dao(FilingDAO.class).create(filing);

        return Database.dao(TaxYearDAO.class).get(id);
    }
}
