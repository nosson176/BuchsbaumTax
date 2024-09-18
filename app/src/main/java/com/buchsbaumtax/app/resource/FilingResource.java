package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.domain.FilingCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.FilingUpdateRequest;
import com.buchsbaumtax.core.model.Filing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sifradigital.framework.auth.Authenticated;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Authenticated
@Path("/filings")
public class FilingResource {
    Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);
    private final ObjectMapper objectMapper;

    public FilingResource() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @GET
    public List<Filing> getAllFilings() {
        return new FilingCRUD().getAll();
    }

    @POST
    public Filing createFiling(Filing filing) {
        return new FilingCRUD().create(filing);
    }

    @GET
    @Path("/{filingId}")
    public Filing getFiling(@PathParam("filingId") int filingId) {
        return new FilingCRUD().get(filingId);
    }

    @GET
    @Path("/tax-year/{taxYearId}")
    public List<Filing> getFilingsByTaxYear(@PathParam("taxYearId") int taxYearId) {
        return new FilingCRUD().getByTaxYear(taxYearId);
    }

    @DELETE
    @Path("/{filingId}")
    public BaseResponse deleteFiling(@PathParam("filingId") int filingId) {
        return new FilingCRUD().delete(filingId);
    }

    @PUT
    @Path("/{filingId}")
    public Filing updateFiling(@PathParam("filingId") int filingId, String filingJson) throws IOException, JsonProcessingException {
        Filing filing = objectMapper.readValue(filingJson, Filing.class);
        return new FilingCRUD().update(filingId, filing);
    }

    @PUT
    @Path("/updateFilings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFilings(FilingUpdateRequest request) {
        try {
            // לוג נתונים לקבלת הבקשה
            logger.info("Received update request with filings: " + request.getUpdates());

            List<Filing> filings = request.getUpdates();

            // קריאה למתודת עדכון הפלטים
            List<Filing> updatedFilings = new FilingCRUD().updateFilingsList(filings);

            // לוג עידכון הפלטים
            logger.info("Updated filings: " + updatedFilings);

            if (updatedFilings.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No filings were updated.")
                        .build();
            }

            return Response.ok(updatedFilings).build();
        } catch (Exception e) {
            // לוג פרטי השגיאה
            logger.error("Error updating filings", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error updating filings: " + e.getMessage())
                    .build();
        }
    }
//    @PUT
//    @Path("/{filingId}")
//    public Filing updateFiling(@PathParam("filingId") int filingId, Filing filing) {
//        return new FilingCRUD().update(filingId, filing);
//    }

    @PUT
    @Path("/updateFiling")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFilingDelivary(FilingUpdateRequest request) {
        List<Filing> updatedFilings = new FilingCRUD().updateFilings(
                request.getClientId(),
                request.getOldContectDelivary(),
                request.getNewContectDelivary()
        );

        return Response.ok(updatedFilings).build();
    }
}
