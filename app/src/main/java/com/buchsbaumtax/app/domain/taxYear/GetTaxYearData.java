package com.buchsbaumtax.app.domain.taxYear;

import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import java.util.List;
import java.util.stream.Collectors;

public class GetTaxYearData {

    public List<TaxYearData> getByClient(int clientId) {
        List<TaxYear> taxYears = Database.dao(TaxYearDAO.class).getByClient(clientId);
        return taxYears.stream().map(TaxYearData::new).collect(Collectors.toList());
    }
}
