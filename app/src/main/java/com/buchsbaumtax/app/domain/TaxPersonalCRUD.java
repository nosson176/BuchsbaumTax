package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class TaxPersonalCRUD {
    public TaxPersonal create(int clientId, TaxPersonal taxPersonal) {
        validate(taxPersonal);
        if (taxPersonal.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        int taxPersonalId = Database.dao(TaxPersonalDAO.class).create(taxPersonal);
        new DisplayFields().setDisplayName(clientId);
        return Database.dao(TaxPersonalDAO.class).get(taxPersonalId);
    }

    public TaxPersonal update(int clientId, int taxPersonalId, TaxPersonal taxPersonal) {
        validate(taxPersonal);
        if (taxPersonal.getId() != taxPersonalId || taxPersonal.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(TaxPersonalDAO.class).update(taxPersonal);
        new DisplayFields().setDisplayName(clientId);
        return Database.dao(TaxPersonalDAO.class).get(taxPersonalId);
    }

    private void validate(TaxPersonal taxPersonal) {
        new Validator()
                .required(taxPersonal.getClientId())
                .validateAndGuard();
    }
}
