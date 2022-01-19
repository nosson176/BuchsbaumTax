package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class SmartviewCRUD {

    public SmartviewData create(User user, SmartviewData smartviewData) {
        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);

        smartview.setUserId(user.getId());
        smartview.setUserName(user.getUsername());
        Smartview created = Database.dao(SmartviewDAO.class).create(smartview);
        return new SmartviewLineUtils().convertToSmartviewData(created);
    }

    public List<SmartviewData> getForUser(User user) {
        List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getByUser(user.getId());
        return smartviews.stream().map(s -> new SmartviewLineUtils().convertToSmartviewData(s)).collect(Collectors.toList());
    }

    public SmartviewData update(User user, int smartviewId, SmartviewData smartviewData) {
        Smartview oldSmartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        if (user.getId() != oldSmartview.getUserId() || smartviewData.getId() != smartviewId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);
        Smartview updated = Database.dao(SmartviewDAO.class).update(smartview);
        return new SmartviewLineUtils().convertToSmartviewData(updated);
    }
}
