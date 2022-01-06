package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ChecklistCRUD;
import com.buchsbaumtax.core.model.Checklist;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.*;
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

    @PUT
    public List<Checklist> updateChecklists(List<Checklist> checklists) {
        return new ChecklistCRUD().update(checklists);
    }

    @PUT
    @Path("/{checklistId}")
    public Checklist updateChecklist(@PathParam("checklistId") int checklistId, Checklist checklist) {
        return new ChecklistCRUD().update(checklistId, checklist);
    }
}
