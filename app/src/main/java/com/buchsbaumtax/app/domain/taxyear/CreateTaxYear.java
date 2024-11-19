package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.app.domain.FilingCRUD;
import com.buchsbaumtax.app.domain.LogCRUD;
import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.Status;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTaxYear {
    private static final Logger logger = LoggerFactory.getLogger(LogCRUD.class);

    public TaxYearData createTaxYear(TaxYearData taxYear) {
        logger.info("taxYear???: {}", taxYear);
        TaxYear year = new TaxYear(taxYear.getClientId(), taxYear.getYear(), taxYear.isArchived(), taxYear.isIrsHistory());
        logger.info("year???: {}", year);
        int id = Database.dao(TaxYearDAO.class).create(year);

        if (taxYear.getFilings() != null && !taxYear.getFilings().isEmpty()) {
            logger.info("isEmpty???: {}");
            FilingCRUD filingCRUD = new FilingCRUD();
            for (Filing filing : taxYear.getFilings()) {
                filing.setTaxYearId(id);
                initializeStatus(filing);
                filingCRUD.create(filing);
            }
        }
        else {
            logger.info("isNOEmpty???: {}");
            Filing filing = new Filing(id, Filing.FILING_TYPE_FEDERAL);
            logger.info("filing???: {}", filing);
            filing.setClientId(taxYear.getClientId());
            initializeStatus(filing);
            int f = Database.dao(FilingDAO.class).create(filing);
            logger.info("f???: {}", f);
        }
        logger.info("id???: {}", id);
        return new TaxYearData(Database.dao(TaxYearDAO.class).get(id));
    }

    private void initializeStatus(Filing filing) {
        // Initialize status with null properties
        Status emptyStatus = new Status(null, null);
        filing.setStatus(emptyStatus);
        filing.setStatusDetail(emptyStatus);
    }
}