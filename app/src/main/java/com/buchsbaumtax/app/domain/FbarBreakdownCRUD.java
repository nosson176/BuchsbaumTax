package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.FbarBreakdownDAO;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class FbarBreakdownCRUD {
    public FbarBreakdown create(int clientId, FbarBreakdown fbarBreakdown) {
        validate(fbarBreakdown);
        if (fbarBreakdown.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        int fbarBreakdownId = Database.dao(FbarBreakdownDAO.class).create(fbarBreakdown);
        FbarBreakdown createdBreakdown = Database.dao(FbarBreakdownDAO.class).get(fbarBreakdownId);
        Double amountUSD = ConvertToUSD.convertToUSD(createdBreakdown.getAmount(), createdBreakdown.getCurrency(), createdBreakdown.getYears());
        createdBreakdown.setAmountUSD(amountUSD);
        return createdBreakdown;
    }

    public FbarBreakdown update(int clientId, int fbarBreakdownId, FbarBreakdown fbarBreakdown) {
        validate(fbarBreakdown);
        if (fbarBreakdown.getId() != fbarBreakdownId || fbarBreakdown.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(FbarBreakdownDAO.class).update(fbarBreakdown);
        FbarBreakdown updatedBreakdown = Database.dao(FbarBreakdownDAO.class).get(fbarBreakdownId);
        Double amountUSD = ConvertToUSD.convertToUSD(updatedBreakdown.getAmount(), updatedBreakdown.getCurrency(), updatedBreakdown.getYears());
        updatedBreakdown.setAmountUSD(amountUSD);
        return updatedBreakdown;
    }

    private void validate(FbarBreakdown fbarBreakdown) {
        new Validator()
                .required(fbarBreakdown.getClientId())
                .validateAndGuard();
    }
}
