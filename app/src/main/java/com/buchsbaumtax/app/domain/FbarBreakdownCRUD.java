package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.FbarBreakdownDAO;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class FbarBreakdownCRUD {
    public FbarBreakdown create(FbarBreakdown fbarBreakdown) {
        validate(fbarBreakdown);
        int fbarBreakdownId = Database.dao(FbarBreakdownDAO.class).create(fbarBreakdown);
        FbarBreakdown createdBreakdown = Database.dao(FbarBreakdownDAO.class).get(fbarBreakdownId);
        Double amountUSD = ConvertToUSD.convertToUSD(createdBreakdown.getAmount(), createdBreakdown.getCurrency(), createdBreakdown.getYears());
        createdBreakdown.setAmountUSD(amountUSD);
        return createdBreakdown;
    }

    public FbarBreakdown update(int fbarBreakdownId, FbarBreakdown fbarBreakdown) {
        validate(fbarBreakdown);
        FbarBreakdown oldBreakdown = Database.dao(FbarBreakdownDAO.class).get(fbarBreakdownId);
        if (fbarBreakdown.getId() != fbarBreakdownId || oldBreakdown == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(FbarBreakdownDAO.class).update(fbarBreakdown);
        FbarBreakdown updatedBreakdown = Database.dao(FbarBreakdownDAO.class).get(fbarBreakdownId);
        Double amountUSD = ConvertToUSD.convertToUSD(updatedBreakdown.getAmount(), updatedBreakdown.getCurrency(), updatedBreakdown.getYears());
        updatedBreakdown.setAmountUSD(amountUSD);
        return updatedBreakdown;
    }

    public BaseResponse bulkUpdate(List<FbarBreakdown> fbarBreakdowns) {
        Database.dao(FbarBreakdownDAO.class).bulkUpdate(fbarBreakdowns);
        return new BaseResponse(true);
    }

    private void validate(FbarBreakdown fbarBreakdown) {
        new Validator()
                .required(fbarBreakdown.getClientId())
                .validateAndGuard();
    }
}
