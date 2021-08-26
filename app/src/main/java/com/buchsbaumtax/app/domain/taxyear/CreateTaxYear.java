package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class CreateTaxYear {

    public TaxYear createTaxYear(int clientId, TaxYear taxYear) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        if (client == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        int id = Database.dao(TaxYearDAO.class).create(taxYear, clientId);
        return Database.dao(TaxYearDAO.class).get(id);
    }
}
