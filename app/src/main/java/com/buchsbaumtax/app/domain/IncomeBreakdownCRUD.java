package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.model.IncomeBreakdown;
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

    public List<IncomeBreakdown> update(List<IncomeBreakdown> incomeBreakdowns) {
        Database.dao(IncomeBreakdownDAO.class).update(incomeBreakdowns);
        return incomeBreakdowns.stream().map(i -> Database.dao(IncomeBreakdownDAO.class).get(i.getId())).collect(Collectors.toList());
    }

    private void validate(IncomeBreakdown incomeBreakdown) {
        new Validator()
                .required(incomeBreakdown.getClientId())
                .validateAndGuard();
    }
}
