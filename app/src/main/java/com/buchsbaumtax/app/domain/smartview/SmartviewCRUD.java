package com.buchsbaumtax.app.domain.smartview;

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
        new UpdateSmartviews().updateSmartview(created);
        return new SmartviewLineUtils().convertToSmartviewData(Database.dao(SmartviewDAO.class).get(created.getId()));
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
        new UpdateSmartviews().updateSmartview(updated);

        if (smartviewData.getSortNumber() != oldSmartview.getSortNumber()) {
            List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getByUser(user.getId());
            smartviews = smartviews.stream().filter(s -> smartviewData.isArchived() == s.isArchived()).collect(Collectors.toList());
            reorder(smartviews, oldSmartview.getSortNumber(), smartviewData.getSortNumber());
        }
        return new SmartviewLineUtils().convertToSmartviewData(Database.dao(SmartviewDAO.class).get(updated.getId()));
    }

    private void reorder(List<Smartview> smartviews, int oldSort, int newSort) {
        // force current order
        for (int i = 0; i < smartviews.size(); i++) {
            smartviews.get(i).setSortNumber(i + 1);
        }

        // reorder
        boolean movedUp = oldSort > newSort;
        for (Smartview s : smartviews) {
            if (movedUp) {
                if (s.getSortNumber() >= newSort && s.getSortNumber() < oldSort) {
                    s.setSortNumber(s.getSortNumber() + 1);
                }
            }
            else {
                if (s.getSortNumber() > oldSort && s.getSortNumber() <= newSort) {
                    s.setSortNumber(s.getSortNumber() - 1);
                }
            }
        }
        Database.dao(SmartviewDAO.class).updateSmartviews(smartviews);
    }
}
