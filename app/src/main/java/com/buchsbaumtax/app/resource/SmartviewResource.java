package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.smartview.SmartviewCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Authenticated
@Path("/smartviews")
public class SmartviewResource {

    @POST
    public SmartviewData createSmartview(@Authenticated User user, SmartviewData smartview, @QueryParam("clientId") Integer clientId) {
        return new SmartviewCRUD().create(user, smartview,clientId);
    }

    @GET
    public List<SmartviewData> getUserSmartviews(@Authenticated User user) {
        return new SmartviewCRUD().getForUser(user);
    }

    @POST
    @Path("/getFilterClients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSmartviewsResults(@Authenticated User user, SmartviewData smartviewData) {
        Map<Client, List<Filing>> clientFilingsMap = new SmartviewCRUD().getSmartViewFiltersResults(smartviewData);

        List<Client> result = clientFilingsMap.entrySet().stream()
                .map(entry -> {
                    Client client = entry.getKey();
                    client.setFilings(entry.getValue());
                    return client;
                })
                .collect(Collectors.toList());

        return Response.ok(result).build();
    }


    @PUT
    @Path("/{smartviewId}")
    public SmartviewData updateSmartview(@Authenticated User user, @PathParam("smartviewId") int smartviewId, SmartviewData smartview) {
        return new SmartviewCRUD().update(user, smartviewId, smartview);
    }

    @DELETE
    @Path("/{smartviewId}")
    public BaseResponse deleteSmartview(@PathParam("smartviewId") int smartviewId) {
        Database.dao(SmartviewDAO.class).delete(smartviewId);
        return new BaseResponse(true);
    }
}
