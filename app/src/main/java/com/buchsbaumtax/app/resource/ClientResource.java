package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ClientCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.auth.Authenticated;

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

}
