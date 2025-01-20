package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.GetClientData;
import com.buchsbaumtax.app.domain.client.CreateClient;
import com.buchsbaumtax.app.domain.client.GetClients;
import com.buchsbaumtax.app.domain.client.UpdateClient;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ClientHistoryDAO;
import com.buchsbaumtax.core.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import java.sql.SQLException;
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
    private static final Logger logger = LoggerFactory.getLogger(ClientResource.class);

    @POST
    public Client createClient(Client client) {
        return new CreateClient().createClient(client);
    }

    @GET
    public List<Client> getAllClients(@QueryParam("smartview") Integer smartviewId,
                                      @QueryParam("q") String q,
                                      @QueryParam("field") String field,
                                      @QueryParam("active") Boolean active) {
        logger.info("Fetching clients with parameters - smartviewId: {}, query: z{}, field: {}, active: {}",
                smartviewId, q, field, active);

        GetClients getClients = new GetClients();

        // Filter by smartview if provided
        if (smartviewId != null) {
            logger.info("Fetching clients for smartviewId: {}", smartviewId);
            return getClients.getForSmartview(smartviewId,active);
        }

        // Perform a search query
        if (q != null) {
            logger.info("Searching clients with query: {}", q);
            if (field != null) {
                logger.info("Searching clients by field: {}", field);
                return getClients.getForFieldSearch(q, field,active);
            }
            return getClients.getForDefaultSearch(q,active);
        }

        // Handle filtering by active status
        boolean isActive = active == null ? true : active; // Default to false if active is not provided
        logger.info("Fetching clients with active status: {}", isActive);
        return Database.dao(ClientDAO.class).getAll(isActive);
    }



    @GET
    @Path("/{clientId}")
    public Client getClient(@PathParam("clientId") int clientId, @QueryParam("active") Boolean active) {
        return Database.dao(ClientDAO.class).get(clientId,active);
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
//        logger.info("updateClient=> {}" ,client);
        return new UpdateClient().updateClient(clientId, client);
    }

    @GET
    @Path("/{clientId}/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaxYearsByClient(@Authenticated User user, @PathParam("clientId") int clientId,@QueryParam("active") Boolean active) {
        logger.debug("Received request to get client data for clientId {} by user {}", clientId, user.getId());

        GetClientData getClientData = new GetClientData();
        ClientData clientData = getClientData.getByClient(user, clientId,active);
//        logger.debug("Received clientData {}", clientData);
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
//            logger.debug("Serialized client data: {}", json);
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

    @POST
    @Path("/exportClients")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportClients(List<Client> clients) {
        try {
            List<CustomerContactInfo> contactInfos = new GetClients().getExportClients(clients);
            return Response.ok(contactInfos).build(); // Ensure that contactInfos is serialized as a JSON array
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching clients: " + e.getMessage())
                    .build();
        }
    }

}
