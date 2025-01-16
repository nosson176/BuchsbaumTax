package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ContactCRUD;
import com.buchsbaumtax.app.domain.TaxPersonalCRUD;
import com.buchsbaumtax.app.domain.taxyear.DeleteTaxYear;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.Contact;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Authenticated
@Path("/personals")
public class TaxPersonalResource {

    @POST
    public TaxPersonal createTaxPersonal(TaxPersonal taxPersonal) {
        return new TaxPersonalCRUD().create(taxPersonal);
    }

    @GET
    public List<TaxPersonal> getAllTaxPersonals() {
        return Database.dao(TaxPersonalDAO.class).getAll();
    }

//    @PUT
//    public List<TaxPersonal> updateTaxPersonals(List<TaxPersonal> taxPersonals) {
//        return new TaxPersonalCRUD().update(taxPersonals);
//    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTaxPersonals(List<TaxPersonal> taxPersonals) {
        new TaxPersonalCRUD().update(taxPersonals);
        return Response.ok("{\"status\":\"success\"}").build();
    }

    @PUT
    @Path("/{personalId}")
    public TaxPersonal updateTaxPersonal(@PathParam("personalId") int taxPersonalId, TaxPersonal taxPersonal) {
        return new TaxPersonalCRUD().update(taxPersonalId, taxPersonal);
    }

    @DELETE
    @Path("/{personalId}")
    public BaseResponse deleteTaxPersonal(@PathParam("personalId") int personalId) {
        return new TaxPersonalCRUD().delete(personalId);
    }
}
