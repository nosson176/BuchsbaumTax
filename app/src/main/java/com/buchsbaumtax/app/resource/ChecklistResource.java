package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ChecklistCRUD;
import com.buchsbaumtax.app.domain.FeeCRUD;
import com.buchsbaumtax.core.model.Checklist;
import com.buchsbaumtax.core.model.Fee;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Authenticated
@Path("/checklists")
public class ChecklistResource {
    @POST
    public Checklist createChecklist(Checklist checklist) {
        return new ChecklistCRUD().create(checklist);
    }

    @GET
    public List<Checklist> getAllChecklists() {
        return new ChecklistCRUD().getAll();
    }

//    @PUT
//    public List<Checklist> updateChecklists(List<Checklist> checklists) {
//        return new ChecklistCRUD().update(checklists);
//    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateChecklists(List<Checklist> checklists) {
        new ChecklistCRUD().update(checklists);
        return Response.ok("{\"status\":\"success\"}").build();
    }

    @PUT
    @Path("/{checklistId}")
    public Checklist updateChecklist(@PathParam("checklistId") int checklistId, Checklist checklist) {
        return new ChecklistCRUD().update(checklistId, checklist);
    }
}
