package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.dao.FeeDAO;
import com.buchsbaumtax.core.model.Contact;
import com.buchsbaumtax.core.model.Fee;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class FeeCRUD {
    public Fee create(Fee fee) {
        int id = Database.dao(FeeDAO.class).create(fee);
        return Database.dao(FeeDAO.class).get(id);
    }

    public List<Fee> getAll() {
        return Database.dao(FeeDAO.class).getAll();
    }

    public Fee update(Fee fee, int feeId) {
        Fee oldFee = Database.dao(FeeDAO.class).get(feeId);
        if (feeId != fee.getId() || oldFee == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(FeeDAO.class).update(fee);
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

//    public List<Fee> update(List<Fee> fees) {
//        Database.dao(FeeDAO.class).update(fees);
//        return fees.stream().map(f -> Database.dao(FeeDAO.class).get(f.getId())).collect(Collectors.toList());
//    }
}
