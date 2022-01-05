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
        if (filing.getId() != filingId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(FilingDAO.class).update(filing);
        return Database.dao(FilingDAO.class).get(filingId);
    }
}
