package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.FilingCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.model.Filing;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/filings")
public class FilingResource {
    @GET
    public List<Filing> getAllFilings() {
        return new FilingCRUD().getAll();
    }

    @POST
    public Filing createFiling(Filing filing) {
        return new FilingCRUD().create(filing);
    }

    @GET
    @Path("/{filingId}")
    public Filing getFiling(@PathParam("filingId") int filingId) {
        return new FilingCRUD().get(filingId);
    }

    @GET
    @Path("/tax-year/{taxYearId}")
    public List<Filing> getFilingsByTaxYear(@PathParam("taxYearId") int taxYearId) {
        return new FilingCRUD().getByTaxYear(taxYearId);
    }

    @DELETE
    @Path("/{filingId}")
    public BaseResponse deleteFiling(@PathParam("filingId") int filingId) {
        return new FilingCRUD().delete(filingId);
    }

    @PUT
    @Path("/{filingId}")
    public Filing updateFiling(@PathParam("filingId") int filingId, Filing filing) {
        return new FilingCRUD().update(filingId, filing);
    }
}
