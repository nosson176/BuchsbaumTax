package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.domain.smartview.UpdateSmartviews;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.dao.FeeDAO;
import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.Contact;
import com.buchsbaumtax.core.model.Fee;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeeCRUD {
    private static final Logger logger = LoggerFactory.getLogger(FeeCRUD.class);
    public Fee create(Fee fee) {
        int id = Database.dao(FeeDAO.class).create(fee);
        return Database.dao(FeeDAO.class).get(id);
    }

    public List<Fee> getAll() {
        return Database.dao(FeeDAO.class).getAll();
    }

    public Fee update(Fee fee, int feeId) {
//        logger.info("Fee update {}: {}");
        Fee oldFee = Database.dao(FeeDAO.class).get(feeId);
//        logger.info("Fee update {}: {}", oldFee);
        if (feeId != fee.getId() || oldFee == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
//        logger.info("Fee update finish {}: {}", oldFee);
        Database.dao(FeeDAO.class).update(fee);
//        logger.info("Fee update finish2222 {}: {}", oldFee);
        return Database.dao(FeeDAO.class).get(feeId);
    }

    public List<Fee> updateFees(List<Fee> fees) {
        for (Fee fee : fees) {
            Fee existingFee = Database.dao(FeeDAO.class).get(fee.getId());

            if (existingFee != null) {
                // If the contact exists, update it
                Database.dao(FeeDAO.class).update(fee);
            } else {
                // If the contact doesn't exist, create a new one
                int newContactId = Database.dao(FeeDAO.class).create(fee);
            }
        }

        // Return the updated list of contacts
        return fees;
    }

    public BaseResponse delete(int feeId){
        try{

            Fee fee = Database.dao(FeeDAO.class).get(feeId);
            if(fee == null){
                return new BaseResponse("Error", "fee not found", Response.Status.NOT_FOUND.getStatusCode());
            }
            Database.dao(FeeDAO.class).delete(feeId);

            // Return success response
            return new BaseResponse("Success", "Fee deleted successfully", Response.Status.OK.getStatusCode());

        }catch (Exception e) {
            // Log the error (optional)
            e.printStackTrace();
            // Return failure response if exception occurs
            return new BaseResponse("Error", "Failed to delete Fee: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }

    }

//    public List<Fee> update(List<Fee> fees) {
//        Database.dao(FeeDAO.class).update(fees);
//        return fees.stream().map(f -> Database.dao(FeeDAO.class).get(f.getId())).collect(Collectors.toList());
//    }
}
