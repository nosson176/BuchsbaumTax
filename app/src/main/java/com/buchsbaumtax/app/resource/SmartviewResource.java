package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Authenticated
@Path("/smartviews")
public class SmartviewResource {
    @POST
    public Smartview createSmartview(Smartview smartview) {
        int id = Database.dao(SmartviewDAO.class).create(smartview);
        return Database.dao(SmartviewDAO.class).get(id);
    }

    @GET
    public List<Smartview> getAllSmartviews() {
        return Database.dao(SmartviewDAO.class).getAll();
    }

    @PUT
    @Path("/{smartviewId}")
    public Smartview updateSmartview(@PathParam("smartviewId") int smartviewId, Smartview smartview) {
        if (smartview.getId() != smartviewId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(SmartviewDAO.class).update(smartview);
        return Database.dao(SmartviewDAO.class).get(smartviewId);
    }
}
