package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UpdateTaxYear {
    public TaxYear updateTaxYear(int taxYearId, int clientId, TaxYear taxYear) {
        if (taxYearId != taxYear.getId() || clientId != taxYear.getClientId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(TaxYearDAO.class).update(taxYear);
        return Database.dao(TaxYearDAO.class).get(taxYearId);
    }
}
