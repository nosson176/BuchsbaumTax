package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.*;
import com.buchsbaumtax.app.domain.taxyear.CreateTaxYear;
import com.buchsbaumtax.app.domain.taxyear.GetClientData;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.*;
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
    @Path("/{clientId}/data")
    public ClientData getTaxYearsByClient(@PathParam("clientId") int clientId) {
        return new GetClientData().getByClient(clientId);
    }

    @DELETE
    @Path("/tax-years/{taxYearId}")
    public BaseResponse deleteTaxYear(@PathParam("taxYearId") int taxYearId) {
        Database.dao(TaxYearDAO.class).delete(taxYearId);
        return new BaseResponse(true);
    }

    // logs CRUD
    @POST
    @Path("/{clientId}/logs")
    public Log createLog(@PathParam("clientId") int clientId, Log log) {
        return new LogCRUD().create(clientId, log);
    }

    @PUT
    @Path("/{clientId}/logs/{id}")
    public Log updateLog(@PathParam("clientId") int clientId, @PathParam("id") int logId, Log log) {
        return new LogCRUD().update(clientId, logId, log);
    }

    @DELETE
    @Path("/{clientId}/logs/{id}")
    public BaseResponse deleteLog(@PathParam("id") int logId) {
        return new LogCRUD().delete(logId);
    }

    // income_breakdowns CRUD
    @POST
    @Path("/{clientId}/income")
    public IncomeBreakdown createIncomeBreakdown(@PathParam("clientId") int clientId, IncomeBreakdown incomeBreakdown) {
        return new IncomeBreakdownCRUD().create(clientId, incomeBreakdown);
    }

    @PUT
    @Path("/{clientId}/income/{id}")
    public IncomeBreakdown updateIncomeBreakdown(@PathParam("clientId") int clientId, @PathParam("id") int incomeBreakdownId, IncomeBreakdown incomeBreakdown) {
        return new IncomeBreakdownCRUD().update(clientId, incomeBreakdownId, incomeBreakdown);
    }

    @DELETE
    @Path("/{clientId}/income/{id}")
    public BaseResponse deleteIncomeBreakdown(@PathParam("id") int incomeBreakdownId) {
        return new IncomeBreakdownCRUD().delete(incomeBreakdownId);
    }

    // fbar_breakdowns CRUD
    @POST
    @Path("/{clientId}/fbar")
    public FbarBreakdown createFbarBreakdown(@PathParam("clientId") int clientId, FbarBreakdown fbarBreakdown) {
        return new FbarBreakdownCRUD().create(clientId, fbarBreakdown);
    }

    @PUT
    @Path("/{clientId}/fbar/{id}")
    public FbarBreakdown updateFbarBreakdown(@PathParam("clientId") int clientId, @PathParam("id") int fbarBreakdownId, FbarBreakdown fbarBreakdown) {
        return new FbarBreakdownCRUD().update(clientId, fbarBreakdownId, fbarBreakdown);
    }

    @DELETE
    @Path("/{clientId}/fbar/{id}")
    public BaseResponse deleteFbarBreakdown(@PathParam("id") int fbarBreakdownId) {
        return new FbarBreakdownCRUD().delete(fbarBreakdownId);
    }

    // contacts CRUD
    @POST
    @Path("/{clientId}/contacts")
    public Contact createContact(@PathParam("clientId") int clientId, Contact contact) {
        return new ContactCRUD().create(clientId, contact);
    }

    @PUT
    @Path("/{clientId}/contacts/{id}")
    public Contact updateContact(@PathParam("clientId") int clientId, @PathParam("id") int contactId, Contact contact) {
        return new ContactCRUD().update(clientId, contactId, contact);
    }

    @DELETE
    @Path("/{clientId}/contacts/{id}")
    public BaseResponse deleteContact(@PathParam("id") int contactId) {
        return new ContactCRUD().delete(contactId);
    }

    // tax_personals CRUD
    @POST
    @Path("/{clientId}/personals")
    public TaxPersonal createTaxPersonal(@PathParam("clientId") int clientId, TaxPersonal taxPersonal) {
        return new TaxPersonalCRUD().create(clientId, taxPersonal);
    }

    @PUT
    @Path("/{clientId}/personals/{id}")
    public TaxPersonal updateTaxPersonal(@PathParam("clientId") int clientId, @PathParam("id") int taxPersonalId, TaxPersonal taxPersonal) {
        return new TaxPersonalCRUD().update(clientId, taxPersonalId, taxPersonal);
    }

    @DELETE
    @Path("/{clientId}/personals/{id}")
    public BaseResponse deleteTaxPersonal(@PathParam("id") int taxPersonalId) {
        return new TaxPersonalCRUD().delete(taxPersonalId);
    }
}
