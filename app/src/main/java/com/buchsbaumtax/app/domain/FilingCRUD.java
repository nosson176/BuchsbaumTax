package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilingCRUD {
    private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);
    public List<Filing> getAll() {
        return Database.dao(FilingDAO.class).getAll();
    }

    public Filing get(int filingId) {
        return Database.dao(FilingDAO.class).get(filingId);
    }

    public List<Filing> getByTaxYear(int taxYearId) {
        return Database.dao(FilingDAO.class).getByTaxYear(taxYearId);
    }

    public Filing create(Filing filing) {
        TaxYear taxYear = Database.dao(TaxYearDAO.class).get(filing.getTaxYearId());
        if (taxYear != null) {
            int clientId = Database.dao(TaxYearDAO.class).get(filing.getTaxYearId()).getClientId();
            if (clientId != 0) {
                filing.setClientId(clientId);
            }
        }

        int id = Database.dao(FilingDAO.class).create(filing);
        return Database.dao(FilingDAO.class).get(id);
    }

    public BaseResponse delete(int filingId) {
        Database.dao(FilingDAO.class).delete(filingId);
        return new BaseResponse(true);
    }

    public Filing update(int filingId, Filing filing) {
        Filing oldFiling = Database.dao(FilingDAO.class).get(filingId);
        if (filing.getId() != filingId || oldFiling == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (oldFiling.getSortOrder() != filing.getSortOrder()) {
            List<Filing> filings = Database.dao(FilingDAO.class).getByTaxYear(filing.getTaxYearId());
            reorder(filings, oldFiling.getSortOrder(), filing.getSortOrder());
        }
        filing.setId(filingId);
        if (filing.getStatusDate() == 0) {
            // Set current Unix timestamp in milliseconds
            filing.setStatusDate(System.currentTimeMillis());
        }
        Database.dao(FilingDAO.class).update(filing);
        return Database.dao(FilingDAO.class).get(filingId);
    }

    public  List<Filing> updateFilingsList(List<Filing> filings){
        logger.info("updateFilingsList1111: {}", filings);
        for (Filing filing : filings) {
            logger.info("updateFilingsEach: {}", filing);
            update(filing.getId(),filing);
        }
        logger.info("updateFilingsList22222: {}", filings);

        return filings;
    }

    private void reorder(List<Filing> filings, int oldSort, int newSort) {

        // force current order
        for (int i = 0; i < filings.size(); i++) {
            filings.get(i).setSortOrder(i + 1);
        }

        // reorder
        boolean movedUp = oldSort > newSort;
        for (Filing filing : filings) {
            if (movedUp) {
                if (filing.getSortOrder() >= newSort && filing.getSortOrder() < oldSort) {
                    filing.setSortOrder(filing.getSortOrder() + 1);
                }
            }
            else {
                if (filing.getSortOrder() > oldSort && filing.getSortOrder() <= newSort) {
                    filing.setSortOrder(filing.getSortOrder() - 1);
                }
            }
        }
        Database.dao(FilingDAO.class).update(filings);
    }

    public List<Filing> updateFilings(int clientId, String oldContectDelivary, String newContectDelivary) {
        logger.info("Request to update filings - ID: {}, Old Delivery: {}, New Delivery: {}",
                clientId, oldContectDelivary, newContectDelivary);

        // Retrieve the list of filings for the given clientId
        List<Filing> filings = Database.dao(FilingDAO.class).getByClient(clientId);
        logger.info("Retrieved filings - ID: {}", filings);

        // Iterate through the filings and update them if they match the old values
        for (Filing filing : filings) {
            // Check if the delivery contact matches the old value
            if (oldContectDelivary.equals(filing.getDeliveryContact())) {
                filing.setDeliveryContact(newContectDelivary);
            }

            // Check if the second delivery contact matches the old value, avoiding NullPointerException
            if (oldContectDelivary.equals(filing.getSecondDeliveryContact())) {
                filing.setSecondDeliveryContact(newContectDelivary);
            }

            // Assuming you have an update method to save the changes to the database
            update(filing.getId(), filing);
        }
        logger.info("Updated filings - ID: {}", filings);
        return filings;
    }

    // Stub methods for illustration (replace with actual database operations)
    private List<Filing> findFilingsByClientId(int clientId) {
        // Implement this method to retrieve filings by clientId from the database
        return new ArrayList<>(); // Return the actual list from the database
    }

    private void updateD(int filingId, Filing filing) {
        // Implement this method to update the filing in the database
    }
}
