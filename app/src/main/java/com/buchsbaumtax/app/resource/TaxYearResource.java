package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.taxyear.CreateTaxYear;
import com.buchsbaumtax.app.domain.taxyear.UpdateTaxYear;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Authenticated
@Path("/tax-years")
public class TaxYearResource {
    @POST
    public TaxYear createTaxYears(TaxYear taxYear) {
        return new CreateTaxYear().createTaxYear(taxYear);
    }

    @PUT
    @Path("/{taxYearId}")
    public TaxYear updateTaxYear(@PathParam("taxYearId") int taxYearId, TaxYear taxYear) {
        return new UpdateTaxYear().updateTaxYear(taxYearId, taxYear);
    }

}