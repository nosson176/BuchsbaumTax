package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.model.Filing;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
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

    public Filing update(int filingId, Filing filing) {
        Filing oldFiling = Database.dao(FilingDAO.class).get(filingId);
        if (filing.getId() != filingId || oldFiling == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(FilingDAO.class).update(filing);
        return Database.dao(FilingDAO.class).get(filingId);
    }

    public BaseResponse bulkUpdate(List<Filing> filings) {
        Database.dao(FilingDAO.class).bulkUpdate(filings);
        return new BaseResponse(true);
    }
}
