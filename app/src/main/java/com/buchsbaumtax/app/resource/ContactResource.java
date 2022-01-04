package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.ContactCRUD;
import com.buchsbaumtax.core.model.Contact;
import com.sifradigital.framework.auth.Authenticated;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Authenticated
@Path("/contacts")
public class ContactResource {
    @POST
    public Contact createContact(Contact contact) {
        return new ContactCRUD().create(contact);
    }

    @PUT
    @Path("/{contactId}")
    public Contact updateContact(@PathParam("contactId") int contactId, Contact contact) {
        return new ContactCRUD().update(contactId, contact);
    }
}
