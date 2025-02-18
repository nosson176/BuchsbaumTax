package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.smartview.SmartviewCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.app.dto.Either;
import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Authenticated
@Path("/smartviews")
public class SmartviewResource {
    private static final Logger logger = LoggerFactory.getLogger(SmartviewResource.class);


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
        Logger logger = LoggerFactory.getLogger(getClass());
        long startTime = System.currentTimeMillis();

        try {
            // Log incoming request
            logger.info("Starting getFilterClients process for user: {}, smartviewData: {}",
                    user.getId(), smartviewData);

            // Step 1: Convert and update smartview
            long conversionStart = System.currentTimeMillis();
            SmartviewCRUD smartviewCRUD = new SmartviewCRUD();
            Map<Client, List<Filing>> clientFilingsMap = smartviewCRUD.getSmartViewFiltersResults(smartviewData);
            logger.info("Smartview conversion and update completed in {}ms",
                    System.currentTimeMillis() - conversionStart);

            // Step 2: Transform results
            long transformStart = System.currentTimeMillis();
            List<Client> result = new ArrayList<>(clientFilingsMap.keySet());
            logger.info("Result transformation completed in {}ms. Total clients: {}",
                    System.currentTimeMillis() - transformStart, result.size());

            // Log overall performance
            logger.info("Total processing time: {}ms", System.currentTimeMillis() - startTime);

            // Add memory usage logging
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
            logger.info("Memory usage after processing: {}MB", usedMemory);

            return Response.ok(result).build();

        } catch (Exception e) {
            logger.error("Error processing getFilterClients request", e);
            return Response.serverError().entity("Error processing request: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{smartviewId}")
    public Either<String, SmartviewData> updateSmartview(
            @Authenticated User user,
            @PathParam("smartviewId") int smartviewId,
            SmartviewData smartview,
            @QueryParam("returnData") @DefaultValue("false") boolean returnData
    ) {
        SmartviewData updatedData = new SmartviewCRUD().update(user, smartviewId, smartview);

        return returnData
                ? Either.right(updatedData)
                : Either.left("Smartview updated successfully");
    }

    @PUT
    @Path("/batchUpdate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBatchSmartviews(@Authenticated User user, List<Smartview> smartviews) {
        logger.info("smartviewsRESOURCE here!!! :{}",smartviews);
        if (smartviews == null || smartviews.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new BaseResponse(false, "No smartviews provided for update"))
                    .build();
        }

        try {
            // Delegate the update to the CRUD service
           new SmartviewCRUD().updateBatch(user, smartviews);

            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new BaseResponse(false, "Failed to update smartviews: " + e.getMessage()))
                    .build();
        }
    }


    @DELETE
    @Path("/{smartviewId}")
    public BaseResponse deleteSmartview(@PathParam("smartviewId") int smartviewId) {
        Database.dao(SmartviewDAO.class).delete(smartviewId);
        return new BaseResponse(true);
    }
}
