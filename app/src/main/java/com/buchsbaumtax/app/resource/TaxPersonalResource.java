package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.TaxPersonalCRUD;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Authenticated
@Path("/personals")
public class TaxPersonalResource {
    @POST
    public TaxPersonal createTaxPersonal(TaxPersonal taxPersonal) {
        return new TaxPersonalCRUD().create(taxPersonal);
    }

    @PUT
    @Path("/{personalId}")
    public TaxPersonal updateTaxPersonal(@PathParam("personalId") int taxPersonalId, TaxPersonal taxPersonal) {
        return new TaxPersonalCRUD().update(taxPersonalId, taxPersonal);
    }
}
