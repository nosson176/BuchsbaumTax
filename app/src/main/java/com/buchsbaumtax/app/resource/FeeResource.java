package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.FeeCRUD;
import com.buchsbaumtax.core.model.Fee;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/fees")
public class FeeResource {
    @POST
    public Fee createFee(Fee fee) {
        return new FeeCRUD().create(fee);
    }

    @GET
    public List<Fee> getAllFees() {
        return new FeeCRUD().getAll();
    }

    @PUT
    public List<Fee> updateFees(List<Fee> fees) {
        return new FeeCRUD().update(fees);
    }

    @PUT
    @Path("/{feeId}")
    public Fee updateFee(@PathParam("feeId") int feeId, Fee fee) {
        return new FeeCRUD().update(fee, feeId);
    }
}
