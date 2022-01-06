package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.model.Contact;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class ContactCRUD {
    public Contact create(Contact contact) {
        validate(contact);
        int contactId = Database.dao(ContactDAO.class).create(contact);
        Contact newContact = Database.dao(ContactDAO.class).get(contactId);
        new DisplayFields().setDisplayPhone(newContact.getClientId());
        return Database.dao(ContactDAO.class).get(contactId);
    }

    public Contact update(int contactId, Contact contact) {
        validate(contact);
        Contact oldContact = Database.dao(ContactDAO.class).get(contactId);
        if (contact.getId() != contactId || oldContact == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(ContactDAO.class).update(contact);
        Contact updatedContact = Database.dao(ContactDAO.class).get(contactId);
        new DisplayFields().setDisplayPhone(updatedContact.getClientId());
        return Database.dao(ContactDAO.class).get(contactId);
    }

    private void validate(Contact contact) {
        new Validator()
                .required(contact.getClientId(), "client is required")
                .validateAndGuard();
    }

    public List<Contact> update(List<Contact> contacts) {
        Database.dao(ContactDAO.class).update(contacts);
        return contacts.stream().map(c -> Database.dao(ContactDAO.class).get(c.getId())).collect(Collectors.toList());
    }
}
