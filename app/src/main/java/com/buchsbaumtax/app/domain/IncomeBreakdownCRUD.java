package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class IncomeBreakdownCRUD {
    public IncomeBreakdown create(int clientId, IncomeBreakdown incomeBreakdown) {
        if (incomeBreakdown.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        validate(incomeBreakdown);
        int incomeBreakdownId = Database.dao(IncomeBreakdownDAO.class).create(incomeBreakdown);
        IncomeBreakdown createdBreakdown = Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdownId);
        Double amountUSD = new ConvertToUSD().convertToUSD(createdBreakdown.getAmount(), createdBreakdown.getCurrency(), createdBreakdown.getYears());
        createdBreakdown.setAmountUSD(amountUSD);
        return createdBreakdown;
    }

    public IncomeBreakdown update(int clientId, int incomeBreakdownId, IncomeBreakdown incomeBreakdown) {
        validate(incomeBreakdown);
        if (incomeBreakdown.getId() != incomeBreakdownId || incomeBreakdown.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(IncomeBreakdownDAO.class).update(incomeBreakdown);
        IncomeBreakdown updatedBreakdown = Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdownId);
        Double amountUSD = new ConvertToUSD().convertToUSD(updatedBreakdown.getAmount(), updatedBreakdown.getCurrency(), updatedBreakdown.getYears());
        updatedBreakdown.setAmountUSD(amountUSD);
        return updatedBreakdown;
    }

    private void validate(IncomeBreakdown incomeBreakdown) {
        new Validator()
                .required(incomeBreakdown.getClientId())
                .validateAndGuard();
    }
}
