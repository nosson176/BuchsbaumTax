package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.FeeDAO;
import com.buchsbaumtax.core.model.Fee;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

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
}
