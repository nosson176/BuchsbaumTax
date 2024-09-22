package com.buchsbaumtax.app.domain.smartview;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartviewCRUD {
    private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

//    public SmartviewData create(User user, SmartviewData smartviewData, Integer clientId) {
//        logger.info("smartviewData IS HERE!: {}", smartviewData);
//        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);
//        logger.info("SmartviewLineUtils IS HERE!: {}", smartview);
//
//        if (clientId != null && clientId > 0) {
//            smartview.setUserId(clientId);
//            smartview.setUserName(smartviewData.getUserName());
//        } else {
//            smartview.setUserId(user.getId());
//            smartview.setUserName(user.getUsername());
//        }
//
//        logger.info("setsmartview IS HERE!: {}", smartview);
//        Smartview created = Database.dao(SmartviewDAO.class).create(smartview);
//        logger.info("created IS HERE!: {}", created);
//        new UpdateSmartviews().updateSmartview(created);
//
//        return new SmartviewLineUtils().convertToSmartviewData(Database.dao(SmartviewDAO.class).get(created.getId()));
//    }

    public SmartviewData create(User user, SmartviewData smartviewData, Integer clientId) {
        logger.info("Creating smartview: {}", smartviewData);

        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);

        if (clientId != null && clientId > 0) {
            smartview.setUserId(clientId);
            smartview.setUserName(smartviewData.getUserName());
        } else {
            smartview.setUserId(user.getId());
            smartview.setUserName(user.getUsername());
        }

        Smartview createdSmartview = Database.dao(SmartviewDAO.class).create(smartview);
        new UpdateSmartviews().updateSmartview(createdSmartview);
        return new SmartviewLineUtils().convertToSmartviewData(createdSmartview);
    }

    public List<SmartviewData> getForUser(User user) {
        List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getByUser(user.getId());
        SmartviewLineUtils smartviewLineUtils = new SmartviewLineUtils();
        return smartviews.stream().map(smartviewLineUtils::convertToSmartviewData).collect(Collectors.toList());
    }

    public SmartviewData update(User user, int smartviewId, SmartviewData smartviewData) {
        Smartview oldSmartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        if (user.getId() != oldSmartview.getUserId() || smartviewData.getId() != smartviewId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        logger.info("oldSmartview IS HERE!: {}", oldSmartview);
        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);
        logger.info("SmartviewLineUtils().convertToSmartview IS HERE!: {}", smartview);

        Smartview updated = Database.dao(SmartviewDAO.class).update(smartview);
        logger.info("Smartview updated IS HERE!: {}", updated);
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

    public Map<Client, List<Filing>> getSmartViewFiltersResults(SmartviewData smartviewData) {
//        Smartview oldSmartview = Database.dao(SmartviewDAO.class).get(smartviewId);
//        if (user.getId() != oldSmartview.getUserId() || smartviewData.getId() != smartviewId) {
//            throw new WebApplicationException(Response.Status.BAD_REQUEST);
//        }
//        logger.info("oldSmartview IS HERE!: {}", oldSmartview);
        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);
        logger.info("SmartviewLineUtils().convertToSmartview IS HERE!: {}", smartview);
//
        Smartview updated = Database.dao(SmartviewDAO.class).update(smartview);
//        logger.info("Smartview updated IS HERE!: {}", updated);
        Map<Client, List<Filing>> data =   new UpdateSmartviews().getSmartviewResult(updated);
        logger.info("Smartview data IS HERE!: {}", data);
        return  data;

//        if (smartviewData.getSortNumber() != oldSmartview.getSortNumber()) {
//            List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getByUser(user.getId());
//            smartviews = smartviews.stream().filter(s -> smartviewData.isArchived() == s.isArchived()).collect(Collectors.toList());
//            reorder(smartviews, oldSmartview.getSortNumber(), smartviewData.getSortNumber());
//        }
//        return new SmartviewLineUtils().convertToSmartviewData(Database.dao(SmartviewDAO.class).get(updated.getId()));
    }
}
