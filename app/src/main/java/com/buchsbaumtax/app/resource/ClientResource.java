package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ClientCRUD;
import com.buchsbaumtax.app.domain.CreateTaxYear;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/clients")
public class ClientResource {

    @POST
    public Client createClient(Client client) {
        return new ClientCRUD().create(client);
    }

    @GET
    public List<Client> getAllClients() {
        return new ClientCRUD().getAll();
    }

    @GET
    @Path("/{clientId}")
    public Client getClient(@PathParam("clientId") int clientId) {
        return new ClientCRUD().get(clientId);
    }

    @DELETE
    @Path("/{clientId}")
    public BaseResponse deleteClient(@PathParam("clientId") int clientId) {
        return new ClientCRUD().delete(clientId);
    }

    @PUT
    @Path("/{clientId}")
    public Client updateClient(@PathParam("clientId") int clientId, Client client) {
        return new ClientCRUD().update(clientId, client);
    }

    @POST
    @Path("/{clientId}/tax-years")
    public TaxYear createTaxYears(@PathParam("clientId") int clientId, TaxYear taxYear) {
        return new CreateTaxYear().createTaxYear(clientId, taxYear);
    }

    @GET
    @Path("/{clientId}/tax-years")
    public List<TaxYear> getTaxYearsByClient(@PathParam("clientId") int clientId) {
        return Database.dao(TaxYearDAO.class).getByClient(clientId);
    }

    @DELETE
    @Path("/tax-years/{taxYearId}")
    public BaseResponse deleteTaxYear(@PathParam("taxYearId") int taxYearId) {
        Database.dao(TaxYearDAO.class).delete(taxYearId);
        return new BaseResponse(true);
    }
}
