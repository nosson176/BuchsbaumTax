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
import com.buchsbaumtax.core.model.ClientWithLogs;
import com.buchsbaumtax.core.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import javax.ws.rs.core.MediaType;
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

    @GET
    @Path("/clientsAndlogs")
    @Produces("application/json")
    public Response clientsAndLogs(@QueryParam("clientId") Long clientId) {
        List<ClientWithLogs> clientWithLogs;

        if (clientId != null) {
            clientWithLogs = GetClientData.getClientAndLogs(clientId);
        } else {
            clientWithLogs = GetClientData.getClientAndLogs(null); // Assuming null should fetch all clients
        }

        return Response.ok(clientWithLogs).build();
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaxYearsByClient(@Authenticated User user, @PathParam("clientId") int clientId) {
        logger.debug("Received request to get client data for clientId {} by user {}", clientId, user.getId());

        GetClientData getClientData = new GetClientData();
        ClientData clientData = getClientData.getByClient(user, clientId);
        logger.debug("Received clientData {}", clientData);
        if (clientData == null) {
            logger.warn("No data found for clientId {}", clientId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client data not found")
                    .build();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            String json = objectMapper.writeValueAsString(clientData);
            logger.debug("Serialized client data: {}", json);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            logger.error("Error serializing client data", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error processing client data")
                    .build();
        }
    }

    @GET
    @Path("/history")
    public List<Client> getClientHistory(@Authenticated User user) {
        return Database.dao(ClientHistoryDAO.class).getRecentByUser(user.getId(), 20);
    }
}
