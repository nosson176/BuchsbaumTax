package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.taxyear.CreateTaxYear;
import com.buchsbaumtax.app.domain.taxyear.UpdateTaxYear;
import com.buchsbaumtax.app.dto.TaxYearData;
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
    public TaxYearData createTaxYears(TaxYearData taxYear) {
        return new CreateTaxYear().createTaxYear(taxYear);
    }

    @GET
    public List<TaxYear> getAllTaxYears() {
        return Database.dao(TaxYearDAO.class).getAll();
    }

    @PUT
    public List<TaxYear> updateTaxYears(List<TaxYear> taxYears) {
        return new UpdateTaxYear().updateTaxYears(taxYears);
    }

    @PUT
    @Path("/{taxYearId}")
    public TaxYear updateTaxYear(@PathParam("taxYearId") int taxYearId, TaxYear taxYear) {
        return new UpdateTaxYear().updateTaxYear(taxYearId, taxYear);
    }
}
