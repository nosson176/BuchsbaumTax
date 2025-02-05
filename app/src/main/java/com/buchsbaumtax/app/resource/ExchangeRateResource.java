package com.buchsbaumtax.app.resource;


import com.buchsbaumtax.app.domain.FbarBreakdownCRUD;
import com.buchsbaumtax.app.domain.user.ExchangeRateCRUD;
import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Authenticated
@Path("/exchangeRate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExchangeRateResource {

    @GET
    public List<ExchangeRate> getAllExchangeRate() {
        return Database.dao(ExchangeRateDAO.class).getAll();
    }

    @GET
    @Path("/{id}")
    public ExchangeRate getExchangeRateById(@PathParam("id") int id) {
        return Database.dao(ExchangeRateDAO.class).getById(id);
    }

    @POST
    public ExchangeRate createExchangeRate(ExchangeRate exchangeRate) {
        return Database.dao(ExchangeRateDAO.class).create(exchangeRate);
    }

    @PUT
    @Path("/{id}")
    public ExchangeRate updateExchangeRate(@PathParam("id") int id, ExchangeRate exchangeRate) {
        ExchangeRate exchangeRateRes = new ExchangeRateCRUD().update(id, exchangeRate);
        return exchangeRateRes;
    }


    @DELETE
    @Path("/{id}")
    public Response deleteExchangeRate(@PathParam("id") int id) {
        Database.dao(ExchangeRateDAO.class).delete(id);
        return Response.noContent().build();
    }
}



