package com.buchsbaumtax.app.domain.smartview;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartviewCRUD {
    private static final Logger logger = LoggerFactory.getLogger(SmartviewCRUD.class);

    public SmartviewData create(User user, SmartviewData smartviewData, Integer clientId) {

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
//        logger.info("smartviews user here!!! :{}",smartviews.size());
        SmartviewLineUtils smartviewLineUtils = new SmartviewLineUtils();
//        logger.info("smartviews final!!! :{}",smartviews.stream().map(smartviewLineUtils::convertToSmartviewData).collect(Collectors.toList()).size());
        return smartviews.stream().map(smartviewLineUtils::convertToSmartviewData).collect(Collectors.toList());
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

    public void updateBatch(User user,List<Smartview>smartviews){
//        logger.info("smartviews here!!! :{}",smartviews);
//        List<Smartview> clientSmartview = Database.dao(SmartviewDAO.class).getByUser(user.getId());
//        if (clientSmartview == null || clientSmartview.isEmpty()) {
//            throw new WebApplicationException(Response.Status.BAD_REQUEST);
//        }
//        for (int i = 0; i < smartviews.size(); i++){
//            if(smartviews.get(i).getId() = clientSmartview.get(i).getId() ){
//                cl
//            }
//        }
            Database.dao(SmartviewDAO.class).updateSmartviewsOrder(smartviews);
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

//    public Map<Client, List<Filing>> getSmartViewFiltersResults(SmartviewData smartviewData) {
//        logger.info("smartviewData: {}", smartviewData);
//        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);
//        logger.info("smartview: {}", smartview);
//        Smartview updated = Database.dao(SmartviewDAO.class).update(smartview);
//        logger.info("smartview updated: {}", updated);
//
////        logger.info("Smartview updated IS HERE!: {}", updated);
//        Map<Client, List<Filing>> data =   new UpdateSmartviews().getSmartviewResult(updated);
//        logger.info("smartview data: {}", data);
//        return  data;
//    }

    public Map<Client, List<Filing>> getSmartViewFiltersResults(SmartviewData smartviewData) {
        logger.info("smartviewData: {}", smartviewData);

        // Convert SmartviewData to Smartview object
        Smartview smartview = new SmartviewLineUtils().convertToSmartview(smartviewData);
        logger.info("smartview: {}", smartview);

        // Initialize the local variable for active
        boolean active = false;

        // Check if there is a smartviewLine with field 'active' and set the value
        for (SmartviewLine line : smartview.getSmartviewLines()) {
            if ("active".equals(line.getField())) {
                active = Boolean.parseBoolean(line.getSearchValue());
                break;  // Exit the loop once we find the 'active' field
            }
        }

//        logger.info("Active status determined: {}", active);

        // Update the smartview in the database
        Smartview updated = Database.dao(SmartviewDAO.class).update(smartview);
        logger.info("smartview updated: {}", updated);

        // Call the function to get results with the active value
        Map<Client, List<Filing>> data = new UpdateSmartviews().getSmartviewResult(updated, active);
        logger.info("smartview data: {}", data);

        return data;
    }

}
