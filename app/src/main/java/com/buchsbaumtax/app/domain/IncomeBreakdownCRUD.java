package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.IncomeBreakdown;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class IncomeBreakdownCRUD {
    public IncomeBreakdown create(IncomeBreakdown incomeBreakdown) {
        validate(incomeBreakdown);
        int incomeBreakdownId = Database.dao(IncomeBreakdownDAO.class).create(incomeBreakdown);
        IncomeBreakdown createdBreakdown = Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdownId);
        Double amountUSD = ConvertToUSD.convertToUSD(createdBreakdown.getAmount(), createdBreakdown.getCurrency(), createdBreakdown.getYears());
        createdBreakdown.setAmountUSD(amountUSD);
        return createdBreakdown;
    }

    public IncomeBreakdown update(int incomeBreakdownId, IncomeBreakdown incomeBreakdown) {
        validate(incomeBreakdown);
        IncomeBreakdown oldBreakdown = Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdownId);
        if (incomeBreakdown.getId() != incomeBreakdownId || oldBreakdown == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(IncomeBreakdownDAO.class).update(incomeBreakdown);
        IncomeBreakdown updatedBreakdown = Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdownId);
        Double amountUSD = ConvertToUSD.convertToUSD(updatedBreakdown.getAmount(), updatedBreakdown.getCurrency(), updatedBreakdown.getYears());
        updatedBreakdown.setAmountUSD(amountUSD);
        return updatedBreakdown;
    }

//    public List<IncomeBreakdown> update(List<IncomeBreakdown> incomeBreakdowns) {
//        Database.dao(IncomeBreakdownDAO.class).update(incomeBreakdowns);
//        return incomeBreakdowns.stream().map(i -> Database.dao(IncomeBreakdownDAO.class).get(i.getId())).collect(Collectors.toList());
//    }
    public List<IncomeBreakdown> update(List<IncomeBreakdown> incomeBreakdowns) {
        return incomeBreakdowns.stream().map(incomeBreakdown -> {
            // Check if the income breakdown exists in the database
            IncomeBreakdown existingBreakdown = Database.dao(IncomeBreakdownDAO.class).get(incomeBreakdown.getId());

            if (existingBreakdown == null) {
                // If it doesn't exist, create a new one using the existing create method
                return create(incomeBreakdown);
            } else {
                // If it exists, update the existing record using the existing update method
                return update(incomeBreakdown.getId(), incomeBreakdown);
            }
        }).collect(Collectors.toList());
    }

    public BaseResponse delete(int incomeId){
        try{

            IncomeBreakdown income = Database.dao(IncomeBreakdownDAO.class).get(incomeId);
            if(income == null){
                return new BaseResponse("Error", "Income not found", Response.Status.NOT_FOUND.getStatusCode());
            }
            Database.dao(IncomeBreakdownDAO.class).delete(incomeId);

            // Return success response
            return new BaseResponse("Success", "Income deleted successfully", Response.Status.OK.getStatusCode());

        }catch (Exception e) {
            // Log the error (optional)
            e.printStackTrace();
            // Return failure response if exception occurs
            return new BaseResponse("Error", "Failed to delete Income: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }

    }


    private void validate(IncomeBreakdown incomeBreakdown) {
        new Validator()
                .required(incomeBreakdown.getClientId())
                .validateAndGuard();
    }
}
