package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.*;
import com.buchsbaumtax.app.domain.taxyear.CreateTaxYear;
import com.buchsbaumtax.app.domain.taxyear.GetClientData;
import com.buchsbaumtax.app.domain.taxyear.UpdateTaxYear;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.ClientHistoryDAO;
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
    public List<Client> getAllClients(@QueryParam("smartview") Integer smartviewId, @QueryParam("q") String q, @QueryParam("field") String field) {
        if (smartviewId != null) {
            return new ClientCRUD().getFiltered(smartviewId);
        }
        if (q != null) {
            if (field != null) {
                return new ClientCRUD().getFiltered(q, field);
            }
            return new ClientCRUD().getFiltered(q);
        }
        return new ClientCRUD().getAll();
    }

    @GET
    @Path("/{clientId}")
    public Client getClient(@PathParam("clientId") int clientId) {
        return new ClientCRUD().get(clientId);
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

    @PUT
    @Path("/{clientId}/tax-years/{id}")
    public TaxYear updateTaxYear(@PathParam("id") int taxYearId, @PathParam("clientId") int clientId, TaxYear taxYear) {
        return new UpdateTaxYear().updateTaxYear(taxYearId, clientId, taxYear);
    }

    @GET
    @Path("/{clientId}/data")
    public ClientData getTaxYearsByClient(@Authenticated User user, @PathParam("clientId") int clientId) {
        return new GetClientData().getByClient(user, clientId);
    }

    @GET
    @Path("/history")
    public List<Client> getClientHistory(@Authenticated User user) {
        return Database.dao(ClientHistoryDAO.class).getRecentByUser(user.getId(), 20);
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

    //filings CRUD
    @GET
    @Path("/filings")
    public List<Filing> getAllFilings() {
        return new FilingCRUD().getAll();
    }

    @GET
    @Path("/filings/{filingId}")
    public Filing getFiling(@PathParam("filingId") int filingId) {
        return new FilingCRUD().get(filingId);
    }

    @GET
    @Path("/filings/tax-year/{taxYearId}")
    public List<Filing> getFilingsByTaxYear(@PathParam("taxYearId") int taxYearId) {
        return new FilingCRUD().getByTaxYear(taxYearId);
    }

    @POST
    @Path("/filings")
    public Filing createFiling(Filing filing) {
        return new FilingCRUD().create(filing);
    }

    @DELETE
    @Path("/filings/{filingId}")
    public BaseResponse deleteFiling(@PathParam("filingId") int filingId) {
        return new FilingCRUD().delete(filingId);
    }

    @PUT
    @Path("/filings/{filingId}")
    public Filing updateFiling(@PathParam("filingId") int filingId, Filing filing) {
        return new FilingCRUD().update(filingId, filing);
    }

    // fees CRUD
    @POST
    @Path("/fees")
    public Fee createFee(Fee fee) {
        return new FeeCRUD().create(fee);
    }

    @GET
    @Path("/fees")
    public List<Fee> getAllFees() {
        return new FeeCRUD().getAll();
    }

    @PUT
    @Path("/fees/{feeId}")
    public Fee updateFee(@PathParam("feeId") int feeId, Fee fee) {
        return new FeeCRUD().update(fee, feeId);
    }

    // checklists CRUD
    @POST
    @Path("/checklists")
    public Checklist createChecklist(Checklist checklist) {
        return new ChecklistCRUD().create(checklist);
    }

    @GET
    @Path("/checklists")
    public List<Checklist> getAllChecklists() {
        return new ChecklistCRUD().getAll();
    }

    @PUT
    @Path("/checklists/{checklistId}")
    public Checklist updateChecklist(@PathParam("checklistId") int checklistId, Checklist checklist) {
        return new ChecklistCRUD().update(checklistId, checklist);
    }
}
