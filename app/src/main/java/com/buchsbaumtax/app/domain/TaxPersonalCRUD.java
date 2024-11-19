package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.Contact;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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

//    public List<TaxPersonal> update(List<TaxPersonal> taxPersonals) {
//        Database.dao(TaxPersonalDAO.class).update(taxPersonals);
//        return taxPersonals.stream().map(t -> Database.dao(TaxPersonalDAO.class).get(t.getId())).collect(Collectors.toList());
//    }

    public List<TaxPersonal> update(List<TaxPersonal> taxPersonals) {
        for (TaxPersonal taxPersonal : taxPersonals) {
            TaxPersonal existingContact = Database.dao(TaxPersonalDAO.class).get(taxPersonal.getId());

            if (existingContact != null) {
                // If the contact exists, update it
                validate(taxPersonal);
                Database.dao(TaxPersonalDAO.class).update(taxPersonal);
            } else {
                // If the contact doesn't exist, create a new one
                validate(taxPersonal);
                int newContactId = Database.dao(TaxPersonalDAO.class).create(taxPersonal);
            }
        }

        // Return the updated list of contacts
        return taxPersonals;
    }

    private void validate(TaxPersonal taxPersonal) {
        new Validator()
                .required(taxPersonal.getClientId())
                .validateAndGuard();
    }
}
