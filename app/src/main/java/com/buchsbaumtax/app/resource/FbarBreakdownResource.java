package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.FbarBreakdownCRUD;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Authenticated
@Path("/fbars")
public class FbarBreakdownResource {
    @POST
    public FbarBreakdown createFbarBreakdown(FbarBreakdown fbarBreakdown) {
        return new FbarBreakdownCRUD().create(fbarBreakdown);
    }

    @PUT
    @Path("/{fbarId}")
    public FbarBreakdown updateFbarBreakdown(@PathParam("fbarId") int fbarBreakdownId, FbarBreakdown fbarBreakdown) {
        return new FbarBreakdownCRUD().update(fbarBreakdownId, fbarBreakdown);
    }
}
