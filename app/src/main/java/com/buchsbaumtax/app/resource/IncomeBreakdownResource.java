package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.IncomeBreakdownCRUD;
import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

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

    @PUT
    public List<IncomeBreakdown> updateIncomeBreakdowns(List<IncomeBreakdown> incomeBreakdowns) {
        return new IncomeBreakdownCRUD().update(incomeBreakdowns);
    }

    @PUT
    @Path("/{incomeId}")
    public IncomeBreakdown updateIncomeBreakdown(@PathParam("incomeId") int incomeBreakdownId, IncomeBreakdown incomeBreakdown) {
        return new IncomeBreakdownCRUD().update(incomeBreakdownId, incomeBreakdown);
    }
}
