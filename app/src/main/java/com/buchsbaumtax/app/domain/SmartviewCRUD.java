package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.dao.SmartviewLineDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

public class SmartviewCRUD {

    public Smartview create(User user, SmartviewData smartviewData) {
        Smartview smartview = new Smartview(null, user.getUsername(), user.getId(), smartviewData.getName(), smartviewData.getSortNumber(), smartviewData.isArchived(), null, null);
        int id = Database.dao(SmartviewDAO.class).create(smartview);
        if (smartviewData.getSmartviewLines() != null) {
            for (SmartviewLine smartviewLine : smartviewData.getSmartviewLines()) {
                smartviewLine.setSmartviewId(id);
                Database.dao(SmartviewLineDAO.class).create(smartviewLine);
            }
        }
        return Database.dao(SmartviewDAO.class).get(id);
    }

    public List<Smartview> getForUser(User user) {
        return Database.dao(SmartviewDAO.class).getByUser(user.getId());
    }

    public Smartview update(User user, int smartviewId, SmartviewData smartviewData) {
        if (smartviewData.getId() != smartviewId || smartviewData.getUserId() != user.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Smartview smartview = new Smartview(smartviewData.getId(), smartviewData.getUserName(), smartviewData.getUserId(), smartviewData.getName(), smartviewData.getSortNumber(), smartviewData.isArchived(), null, new Date());
        Database.dao(SmartviewDAO.class).update(smartview);

        if (smartviewData.getSmartviewLines() != null) {
            for (SmartviewLine smartviewLine : smartviewData.getSmartviewLines()) {
                if (smartviewLine.getId() == null) {
                    smartviewLine.setSmartviewId(smartviewId);
                    Database.dao(SmartviewLineDAO.class).create(smartviewLine);
                }
                else if (smartviewLine.getSmartviewId() == smartviewId) {
                    Database.dao(SmartviewLineDAO.class).update(smartviewLine);
                }
            }
        }

        return Database.dao(SmartviewDAO.class).get(smartviewId);
    }
}
