package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.domain.GetClientData;
import com.buchsbaumtax.app.domain.client.CreateClient;
import com.buchsbaumtax.app.domain.client.GetClients;
import com.buchsbaumtax.app.domain.client.UpdateClient;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ClientHistoryDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Authenticated
@Path("/clients")
public class ClientResource {
    private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

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
    public Response getTaxYearsByClient(@Authenticated User user, @PathParam("clientId") int clientId, @Context UriInfo uriInfo) {
        logger.debug("Received request to get client data for clientId {} by user {}", clientId, user.getId());

        GetClientData getClientData = new GetClientData();
        ClientData clientData = getClientData.getByClient(user, clientId);

        if (clientData == null) {
            logger.warn("No data found for clientId {}", clientId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client data not found")
                    .build();
        }

        logger.info("Client data retrieved successfully for clientId {}: {}", clientId, clientData.getContacts().size());

        // Build the response with the retrieved client data
        return Response.ok(clientData).build();
    }

    @GET
    @Path("/history")
    public List<Client> getClientHistory(@Authenticated User user) {
        return Database.dao(ClientHistoryDAO.class).getRecentByUser(user.getId(), 20);
    }
}
