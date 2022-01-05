package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class UpdateTaxYear {
    public TaxYear updateTaxYear(int taxYearId, TaxYear taxYear) {
        TaxYear oldTaxYear = Database.dao(TaxYearDAO.class).get(taxYearId);
        if (taxYearId != taxYear.getId() || oldTaxYear == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(TaxYearDAO.class).update(taxYear);
        return Database.dao(TaxYearDAO.class).get(taxYearId);
    }

    public BaseResponse bulkUpdate(List<TaxYear> taxYears) {
        Database.dao(TaxYearDAO.class).bulkUpdate(taxYears);
        return new BaseResponse(true);
    }
}
