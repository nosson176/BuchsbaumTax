package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ClientCRUD;
import com.buchsbaumtax.app.domain.taxyear.GetClientData;
import com.buchsbaumtax.app.dto.ClientData;
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
        return new ClientCRUD().create(client);
    }

    @GET
    public List<Client> getAllClients(@QueryParam("smartview") Integer smartviewId, @QueryParam("q") String q) {
        if (smartviewId != null) {
            return new ClientCRUD().getFiltered(smartviewId);
        }
        if (q != null) {
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
