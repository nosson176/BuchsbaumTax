package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class SmartviewCRUD {

    public Smartview create(User user, Smartview smartview) {
        return Database.dao(SmartviewDAO.class).create(user, smartview);
    }

    public List<Smartview> getForUser(User user) {
        return Database.dao(SmartviewDAO.class).getByUser(user.getId());
    }

    public Smartview update(User user, int smartviewId, Smartview smartview) {
        Smartview oldSmartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        if (user.getId() != oldSmartview.getUserId() || smartview.getId() != smartviewId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return Database.dao(SmartviewDAO.class).update(smartview);
    }
}
