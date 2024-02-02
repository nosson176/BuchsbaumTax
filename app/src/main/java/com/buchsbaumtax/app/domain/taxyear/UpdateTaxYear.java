package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateTaxYear {
    public TaxYear updateTaxYear(int taxYearId, TaxYear taxYear) {
        TaxYear oldTaxYear = Database.dao(TaxYearDAO.class).get(taxYearId);
        if (taxYearId != taxYear.getId() || oldTaxYear == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(TaxYearDAO.class).update(taxYear);
        return Database.dao(TaxYearDAO.class).get(taxYearId);
    }

    public List<TaxYear> updateTaxYears(List<TaxYear> taxYears) {
        Database.dao(TaxYearDAO.class).update(taxYears);
        return taxYears.stream().map(t -> Database.dao(TaxYearDAO.class).get(t.getId())).collect(Collectors.toList());
    }
}
