package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ContactCRUD;
import com.buchsbaumtax.app.domain.FeeCRUD;
import com.buchsbaumtax.app.domain.TaxPersonalCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.model.Contact;
import com.buchsbaumtax.core.model.Fee;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

//    @PUT
//    public List<Fee> updateFees(List<Fee> fees) {
//        return new FeeCRUD().update(fees);
//    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFees(List<Fee> fees) {
        new FeeCRUD().updateFees(fees);
        return Response.ok("{\"status\":\"success\"}").build();
    }

    @PUT
    @Path("/{feeId}")
    public Fee updateFee(@PathParam("feeId") int feeId, Fee fee) {
        return new FeeCRUD().update(fee, feeId);
    }

    @DELETE
    @Path("/{feeId}")
    public BaseResponse deleteFee(@PathParam("feeId") int feeId) {
        return new FeeCRUD().delete(feeId);
    }
}


