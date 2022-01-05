package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class TaxPersonalCRUD {
    public TaxPersonal create(TaxPersonal taxPersonal) {
        validate(taxPersonal);
        int taxPersonalId = Database.dao(TaxPersonalDAO.class).create(taxPersonal);
        TaxPersonal newPersonal = Database.dao(TaxPersonalDAO.class).get(taxPersonalId);
        new DisplayFields().setDisplayName(newPersonal.getClientId());
        return Database.dao(TaxPersonalDAO.class).get(taxPersonalId);
    }

    public TaxPersonal update(int taxPersonalId, TaxPersonal taxPersonal) {
        validate(taxPersonal);
        TaxPersonal oldTaxPersonal = Database.dao(TaxPersonalDAO.class).get(taxPersonalId);
        if (taxPersonal.getId() != taxPersonalId || oldTaxPersonal == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(TaxPersonalDAO.class).update(taxPersonal);
        TaxPersonal updatePersonal = Database.dao(TaxPersonalDAO.class).get(taxPersonalId);
        new DisplayFields().setDisplayName(updatePersonal.getClientId());
        return Database.dao(TaxPersonalDAO.class).get(taxPersonalId);
    }

    public BaseResponse bulkUpdate(List<TaxPersonal> taxPersonals) {
        Database.dao(TaxPersonalDAO.class).bulkUpdate(taxPersonals);
        return new BaseResponse(true);
    }

    private void validate(TaxPersonal taxPersonal) {
        new Validator()
                .required(taxPersonal.getClientId())
                .validateAndGuard();
    }
}
