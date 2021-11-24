package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.model.Contact;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ContactCRUD {
    public Contact create(int clientId, Contact contact) {
        validate(contact);
        if (contact.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        int contactId = Database.dao(ContactDAO.class).create(contact);
        new DisplayFields().setDisplayPhone(clientId);
        return Database.dao(ContactDAO.class).get(contactId);
    }

    public Contact update(int clientId, int contactId, Contact contact) {
        validate(contact);
        if (contact.getId() != contactId || contact.getClientId() != clientId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(ContactDAO.class).update(contact);
        new DisplayFields().setDisplayPhone(clientId);
        return Database.dao(ContactDAO.class).get(contactId);
    }

    private void validate(Contact contact) {
        new Validator()
                .required(contact.getClientId(), "client is required")
                .validateAndGuard();
    }
}
