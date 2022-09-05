package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class FilingCRUD {

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
        Database.dao(FilingDAO.class).update(filing);
        return Database.dao(FilingDAO.class).get(filingId);
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
}
