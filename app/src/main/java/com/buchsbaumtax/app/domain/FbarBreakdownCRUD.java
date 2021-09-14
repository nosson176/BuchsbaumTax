package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
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
        return Database.dao(FbarBreakdownDAO.class).get(fbarBreakdownId);
    }

    public FbarBreakdown update(int clientId, int fbarBreakdownId, FbarBreakdown fbarBreakdown) {
        validate(fbarBreakdown);
        if (fbarBreakdown.getId() != fbarBreakdownId || fbarBreakdown.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(FbarBreakdownDAO.class).update(fbarBreakdown);
        return Database.dao(FbarBreakdownDAO.class).get(fbarBreakdownId);
    }

    public BaseResponse delete(int fbarBreakdownId) {
        Database.dao(FbarBreakdownDAO.class).delete(fbarBreakdownId);
        return new BaseResponse(true);
    }

    private void validate(FbarBreakdown fbarBreakdown) {
        new Validator()
                .required(fbarBreakdown.getClientId())
                .validateAndGuard();
    }
}
