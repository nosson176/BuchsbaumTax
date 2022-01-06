package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ContactCRUD;
import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.model.Contact;
import com.sifradigital.framework.auth.Authenticated;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.*;
import java.util.List;

@Authenticated
@Path("/contacts")
public class ContactResource {
    @POST
    public Contact createContact(Contact contact) {
        return new ContactCRUD().create(contact);
    }

    @GET
    public List<Contact> getAllContacts() {
        return Database.dao(ContactDAO.class).getAll();
    }

    @PUT
    public List<Contact> updateContacts(List<Contact> contacts) {
        return new ContactCRUD().update(contacts);
    }

    @PUT
    @Path("/{contactId}")
    public Contact updateContact(@PathParam("contactId") int contactId, Contact contact) {
        return new ContactCRUD().update(contactId, contact);
    }
}
