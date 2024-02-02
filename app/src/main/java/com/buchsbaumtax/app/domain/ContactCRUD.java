package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.model.Contact;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

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
        if (oldContact.getSortOrder() != contact.getSortOrder()) {
            List<Contact> contacts = Database.dao(ContactDAO.class).getForClient(contact.getClientId());
            reorder(contacts, oldContact, contact.getSortOrder());
        }
        Database.dao(ContactDAO.class).update(contact);
        Contact updatedContact = Database.dao(ContactDAO.class).get(contactId);
        new DisplayFields().setDisplayPhone(updatedContact.getClientId());
        return Database.dao(ContactDAO.class).get(contactId);
    }

    private void validate(Contact contact) {
        new Validator()
                .required(contact.getClientId(), "Client ID is required")
                .validateAndGuard();
    }

    private void reorder(List<Contact> contacts, Contact oldContact, int newSort) {
        if (newSort == 0) {
            contacts.forEach(c -> c.setSortOrder(0));
            Database.dao(ContactDAO.class).update(contacts);
            return;
        }

        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).setSortOrder(i + 1);
        }

        int oldSort = oldContact.getSortOrder() == 0 ? contacts.stream().filter(c -> c.getId() == oldContact.getId()).findAny().get().getSortOrder() : oldContact.getSortOrder();

        boolean movedUp = oldSort > newSort;

        for (Contact contact : contacts) {
            if (movedUp) {
                if (contact.getSortOrder() >= newSort && contact.getSortOrder() < oldSort) {
                    contact.setSortOrder(contact.getSortOrder() + 1);
                }
            }
            else {
                if (contact.getSortOrder() > oldSort && contact.getSortOrder() <= newSort) {
                    contact.setSortOrder(contact.getSortOrder() - 1);
                }
            }
        }
        Database.dao(ContactDAO.class).update(contacts);
    }
}
