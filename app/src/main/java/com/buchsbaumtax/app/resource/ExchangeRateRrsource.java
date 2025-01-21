package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ContactCRUD;
import com.buchsbaumtax.app.domain.TaxPersonalCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.dao.ExchangeRateDAO;
import com.buchsbaumtax.core.model.Contact;
import com.buchsbaumtax.core.model.ExchangeRate;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Authenticated
@Path("/exchangeRate")
public class ExchangeRateRrsource {

    @GET
    public List<ExchangeRate> getAllExchangeRate() {
        return Database.dao(ExchangeRateDAO.class).getAll();
    }

    @POST
    public int createExchangeRate(ExchangeRate exchangeRate) {
        return Database.dao(ExchangeRateDAO.class).create(exchangeRate);
    }
}
