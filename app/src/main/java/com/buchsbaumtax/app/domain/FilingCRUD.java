package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.model.Filing;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class FilingCRUD {
    public List<Filing> getAll() {
        return Database.dao(FilingDAO.class).getAll();
    }

    public Filing get(int filingId) {
        return Database.dao(FilingDAO.class).get(filingId);
    }

    public List<Filing> getByTaxYear(int taxYearId) {
        return Database.dao(FilingDAO.class).getByTaxYear(taxYearId);
    }

    public Filing create(Filing filing) {
        int id = Database.dao(FilingDAO.class).create(filing);
        return Database.dao(FilingDAO.class).get(id);
    }

    public BaseResponse delete(int filingId) {
        Database.dao(FilingDAO.class).delete(filingId);
        return new BaseResponse(true);
    }
}
