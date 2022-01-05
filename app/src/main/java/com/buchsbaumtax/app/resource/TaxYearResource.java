package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.taxyear.CreateTaxYear;
import com.buchsbaumtax.app.domain.taxyear.UpdateTaxYear;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/tax-years")
public class TaxYearResource {
    @POST
    public TaxYear createTaxYears(TaxYear taxYear) {
        return new CreateTaxYear().createTaxYear(taxYear);
    }

    @GET
    public List<TaxYear> getAllTaxYears() {
        return Database.dao(TaxYearDAO.class).getAll();
    }

    @PUT
    public BaseResponse bulkUpdateTaxYears(List<TaxYear> taxYears) {
        return new UpdateTaxYear().bulkUpdate(taxYears);
    }

    @PUT
    @Path("/{taxYearId}")
    public TaxYear updateTaxYear(@PathParam("taxYearId") int taxYearId, TaxYear taxYear) {
        return new UpdateTaxYear().updateTaxYear(taxYearId, taxYear);
    }

}
