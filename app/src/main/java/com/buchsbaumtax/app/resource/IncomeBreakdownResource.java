package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.IncomeBreakdownCRUD;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Authenticated
@Path("/incomes")
public class IncomeBreakdownResource {
    @POST
    public IncomeBreakdown createIncomeBreakdown(IncomeBreakdown incomeBreakdown) {
        return new IncomeBreakdownCRUD().create(incomeBreakdown);
    }

    @PUT
    @Path("/{incomeId}")
    public IncomeBreakdown updateIncomeBreakdown(@PathParam("incomeId") int incomeBreakdownId, IncomeBreakdown incomeBreakdown) {
        return new IncomeBreakdownCRUD().update(incomeBreakdownId, incomeBreakdown);
    }
}
