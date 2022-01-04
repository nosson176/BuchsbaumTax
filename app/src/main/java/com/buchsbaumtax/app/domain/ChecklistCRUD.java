package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ChecklistDAO;
import com.buchsbaumtax.core.model.Checklist;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class ChecklistCRUD {
    public Checklist create(Checklist checklist) {
        int id = Database.dao(ChecklistDAO.class).create(checklist);
        return Database.dao(ChecklistDAO.class).get(id);
    }

    public List<Checklist> getAll() {
        return Database.dao(ChecklistDAO.class).getAll();
    }

    public Checklist update(int checklistId, Checklist checklist) {
        Checklist oldChecklist = Database.dao(ChecklistDAO.class).get(checklistId);
        if (checklist.getId() != checklistId || oldChecklist == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(ChecklistDAO.class).update(checklist);
        return Database.dao(ChecklistDAO.class).get(checklistId);
    }
}
