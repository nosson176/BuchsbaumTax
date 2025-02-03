package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.IncomeBreakdownCRUD;
import com.buchsbaumtax.app.domain.TaxPersonalCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.FbarBreakdownDAO;
import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Authenticated
@Path("/incomes")
public class IncomeBreakdownResource {
    @POST
    public IncomeBreakdown createIncomeBreakdown(IncomeBreakdown incomeBreakdown) {
        return new IncomeBreakdownCRUD().create(incomeBreakdown);
    }

    @GET
    public List<IncomeBreakdown> getAllIncomeBreakdowns() {
        return Database.dao(IncomeBreakdownDAO.class).getAll();
    }

    @GET
    @Path("/restIncomes/{clientId}")
    public List<IncomeBreakdown> getARestIncomesByClient(@PathParam("clientId") int clientId) {
        LocalDateTime threeYearsAgoStartOfYear = LocalDateTime.now()
                .minusYears(3) // Go back three years
                .withDayOfYear(1) // Set to the first day of the year
                .withHour(0).withMinute(0).withSecond(0).withNano(0); // Reset time to midnight

        Instant threeYearsAgoInstant = threeYearsAgoStartOfYear.toInstant(ZoneOffset.UTC);
        long threeYearsAgoStart = threeYearsAgoInstant.toEpochMilli();
        return Database.dao(IncomeBreakdownDAO.class).getIncomeBreakdownsBeforeLastThreeYears(clientId,threeYearsAgoStart);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateIncomeBreakdowns(List<IncomeBreakdown> incomeBreakdowns) {
        try {
            // Update income breakdowns using the CRUD class
            new IncomeBreakdownCRUD().update(incomeBreakdowns);

            // Return a response with "status: success"
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");

            return Response.ok(response).build();
        } catch (Exception e) {
            // Handle any exception by returning an error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating income breakdowns: " + e.getMessage())
                    .build();
        }
    }


    @PUT
    @Path("/{incomeId}")
    public IncomeBreakdown updateIncomeBreakdown(@PathParam("incomeId") int incomeBreakdownId, IncomeBreakdown incomeBreakdown) {
        return new IncomeBreakdownCRUD().update(incomeBreakdownId, incomeBreakdown);
    }

    @DELETE
    @Path("/{incomeId}")
    public BaseResponse deleteTaxPersonal(@PathParam("incomeId") int incomeId) {
        return new IncomeBreakdownCRUD().delete(incomeId);
    }
}
