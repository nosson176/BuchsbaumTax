package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
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
        return Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdownId);
    }

    public IncomeBreakdown update(int clientId, int incomeBreakdownId, IncomeBreakdown incomeBreakdown) {
        validate(incomeBreakdown);
        if (incomeBreakdown.getId() != incomeBreakdownId || incomeBreakdown.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(IncomeBreakdownDAO.class).update(incomeBreakdown);
        return Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdownId);
    }

    public BaseResponse delete(int incomeBreakdownId) {
        Database.dao(IncomeBreakdownDAO.class).delete(incomeBreakdownId);
        return new BaseResponse(true);
    }

    private void validate(IncomeBreakdown incomeBreakdown) {
        new Validator()
                .required(incomeBreakdown.getClientId())
                .validateAndGuard();
    }
}
