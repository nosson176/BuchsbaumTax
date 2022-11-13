package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.client.CreateClient;
import com.buchsbaumtax.app.domain.client.GetClients;
import com.buchsbaumtax.app.domain.client.UpdateClient;
import com.buchsbaumtax.app.domain.GetClientData;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ClientHistoryDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/clients")
public class ClientResource {

    @POST
    public Client createClient(Client client) {
        return new CreateClient().createClient(client);
    }

    @GET
    public List<Client> getAllClients(@QueryParam("smartview") Integer smartviewId, @QueryParam("q") String q, @QueryParam("field") String field) {
        GetClients getClients = new GetClients();
        if (smartviewId != null) {
            return getClients.getForSmartview(smartviewId);
        }
        if (q != null) {
            if (field != null) {
                return getClients.getForFieldSearch(q, field);
            }
            return getClients.getForDefaultSearch(q);
        }
        return getClients.getAll();
    }

    @GET
    @Path("/{clientId}")
    public Client getClient(@PathParam("clientId") int clientId) {
        return Database.dao(ClientDAO.class).get(clientId);
    }

    @DELETE
    @Path("/{clientId}")
    public BaseResponse deleteClient(@PathParam("clientId") int clientId) {
        Database.dao(ClientDAO.class).delete(clientId);
        return new BaseResponse(true);
    }

    @PUT
    @Path("/{clientId}")
    public Client updateClient(@PathParam("clientId") int clientId, Client client) {
        return new UpdateClient().updateClient(clientId, client);
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
}
