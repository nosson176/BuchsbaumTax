package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.FbarBreakdownCRUD;
import com.buchsbaumtax.app.domain.IncomeBreakdownCRUD;
import com.buchsbaumtax.app.dto.Either;
import com.buchsbaumtax.core.dao.FbarBreakdownDAO;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Authenticated
@Path("/fbars")
public class FbarBreakdownResource {
    @POST
    public FbarBreakdown createFbarBreakdown(FbarBreakdown fbarBreakdown) {
        return new FbarBreakdownCRUD().create(fbarBreakdown);
    }

    @GET
    public List<FbarBreakdown> getAllFbarBreakdowns() {
        return Database.dao(FbarBreakdownDAO.class).getAll();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFbarsBreakdowns(List<FbarBreakdown> fbarBreakdowns) {
        try {
            // Update income breakdowns using the CRUD class
            new FbarBreakdownCRUD().update(fbarBreakdowns);

            // Return a response with "status: success"
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");

            return Response.ok(response).build();
        } catch (Exception e) {
            // Handle any exception by returning an error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating fbar breakdowns: " + e.getMessage())
                    .build();
        }
    }

//    @PUT
//    @Path("/{fbarId}")
//    public FbarBreakdown updateFbarBreakdown(@PathParam("fbarId") int fbarBreakdownId, FbarBreakdown fbarBreakdown) {
//        return new FbarBreakdownCRUD().update(fbarBreakdownId, fbarBreakdown);
//    }

    @PUT
    @Path("/{fbarId}")
    public Either<String, FbarBreakdown> updateFbarBreakdown(
            @PathParam("fbarId") int fbarBreakdownId,
            FbarBreakdown fbarBreakdown,
            @QueryParam("returnData") @DefaultValue("true") boolean returnData
    ) {
        FbarBreakdown updatedBreakdown = new FbarBreakdownCRUD().update(fbarBreakdownId, fbarBreakdown);

        return returnData
                ? Either.right(updatedBreakdown)
                : Either.left("Fbar Breakdown updated successfully");
    }
}
