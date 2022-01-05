package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.FbarBreakdownCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Authenticated
@Path("/fbars")
public class FbarBreakdownResource {
    @POST
    public FbarBreakdown createFbarBreakdown(FbarBreakdown fbarBreakdown) {
        return new FbarBreakdownCRUD().create(fbarBreakdown);
    }

    @PUT
    public BaseResponse bulkUpdateFbarBreakdowns(List<FbarBreakdown> fbarBreakdowns) {
        return new FbarBreakdownCRUD().bulkUpdate(fbarBreakdowns);
    }

    @PUT
    @Path("/{fbarId}")
    public FbarBreakdown updateFbarBreakdown(@PathParam("fbarId") int fbarBreakdownId, FbarBreakdown fbarBreakdown) {
        return new FbarBreakdownCRUD().update(fbarBreakdownId, fbarBreakdown);
    }
}
