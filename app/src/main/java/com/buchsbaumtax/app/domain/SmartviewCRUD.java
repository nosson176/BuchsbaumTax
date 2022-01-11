package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class SmartviewCRUD {

    public Smartview create(User user, SmartviewData smartviewData) {
        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);

        smartview.setUserId(user.getId());
        smartview.setUserName(user.getUsername());
        return Database.dao(SmartviewDAO.class).create(smartview);
    }

    public List<SmartviewData> getForUser(User user) {
        List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getByUser(user.getId());
        List<SmartviewData> smartviewData = new ArrayList<>();

        for (Smartview smartview : smartviews) {
            SmartviewData smartviewDataObject = new SmartviewLineUtils().convertToSmartviewData(smartview);
            smartviewData.add(smartviewDataObject);
        }

        return smartviewData;
    }

    public Smartview update(User user, int smartviewId, SmartviewData smartviewData) {
        Smartview oldSmartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        if (user.getId() != oldSmartview.getUserId() || smartviewData.getId() != smartviewId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);
        return Database.dao(SmartviewDAO.class).update(smartview);
    }
}
