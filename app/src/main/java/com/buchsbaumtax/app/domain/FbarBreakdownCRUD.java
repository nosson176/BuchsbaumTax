package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.FbarBreakdownDAO;
import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.model.FbarBreakdown;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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

//    public List<FbarBreakdown> update(List<FbarBreakdown> fbarBreakdowns) {
//        Database.dao(FbarBreakdownDAO.class).update(fbarBreakdowns);
//        return fbarBreakdowns.stream().map(f -> Database.dao(FbarBreakdownDAO.class).get(f.getId())).collect(Collectors.toList());
//    }
public List<FbarBreakdown> update(List<FbarBreakdown> fbarBreakdowns) {
    return fbarBreakdowns.stream().map(fbarBreakdown -> {
        // Check if the income breakdown exists in the database
        FbarBreakdown existingBreakdown = Database.dao(FbarBreakdownDAO.class).get(fbarBreakdown.getId());

        if (existingBreakdown == null) {
            // If it doesn't exist, create a new one using the existing create method
            return create(fbarBreakdown);
        } else {
            // If it exists, update the existing record using the existing update method
            return update(fbarBreakdown.getId(), fbarBreakdown);
        }
    }).collect(Collectors.toList());
}

    private void validate(FbarBreakdown fbarBreakdown) {
        new Validator()
                .required(fbarBreakdown.getClientId())
                .validateAndGuard();
    }
}
