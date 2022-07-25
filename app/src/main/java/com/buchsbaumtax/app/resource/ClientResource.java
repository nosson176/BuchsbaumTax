package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.client.CreateClient;
import com.buchsbaumtax.app.domain.client.GetClients;
import com.buchsbaumtax.app.domain.client.UpdateClient;
import com.buchsbaumtax.app.domain.taxyear.GetClientData;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ClientFlagDAO;
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
    ClientFlagDAO clientFlagDAO = Database.dao(ClientFlagDAO.class);

    @POST
    public Client createClient(Client client) {
        return new CreateClient().createClient(client);
    }

    @GET
    public List<Client> getAllClients(@Authenticated User user, @QueryParam("smartview") Integer smartviewId, @QueryParam("q") String q, @QueryParam("field") String field) {
        GetClients getClients = new GetClients();
        if (smartviewId != null) {
            return getClients.getForSmartview(user.getId(), smartviewId);
        }
        if (q != null) {
            if (field != null) {
                return getClients.getForFieldSearch(q, field, user.getId());
            }
            return getClients.getForDefaultSearch(q, user.getId());
        }
        return getClients.getAllByUser(user.getId());
    }

    @GET
    @Path("/{clientId}")
    public Client getClient(@Authenticated User user, @PathParam("clientId") int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        client.setFlag(Database.dao(ClientFlagDAO.class).getFlagForUserClient(user.getId(), clientId));
        return client;
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
