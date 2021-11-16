package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ContactDAO;
import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Contact;
import com.buchsbaumtax.core.model.TaxPersonal;
import com.sifradigital.framework.db.Database;

import java.util.List;
import java.util.Objects;

public class DisplayFields {
    public void setDisplayName(int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        List<TaxPersonal> taxPersonals = Database.dao(TaxPersonalDAO.class).getForClient(clientId);
        String displayName = getDisplayName(taxPersonals);

        client.setDisplayName(displayName);
        Database.dao(ClientDAO.class).update(client);
    }

    public String getDisplayName(List<TaxPersonal> taxPersonals) {
        TaxPersonal primary = taxPersonals.stream()
                .filter(tp -> Objects.nonNull(tp.getCategory()))
                .filter(tp -> tp.getCategory().equals("PRI."))
                .findFirst()
                .orElse(null);
        TaxPersonal secondary = taxPersonals.stream()
                .filter(tp -> Objects.nonNull(tp.getCategory()))
                .filter(tp -> tp.getCategory().equals("SEC."))
                .findFirst()
                .orElse(null);

        boolean primaryExists = primary != null && primary.getFirstName() != null;
        boolean secondaryExists = secondary != null && secondary.getFirstName() != null;
        if (primaryExists && secondaryExists) {
            return primary.getFirstName() + " - " + secondary.getFirstName();
        }
        else if (primaryExists) {
            return primary.getFirstName();
        }
        return null;
    }

    public void setDisplayPhone(int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        String displayPhone = getDisplayPhone(clientId);
        client.setDisplayPhone(displayPhone);
        Database.dao(ClientDAO.class).update(client);
    }

    public String getDisplayPhone(int clientId) {
        List<Contact> contacts = Database.dao(ContactDAO.class).getForClient(clientId);
        return contacts.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(Contact::getMainDetail)
                .orElse(null);
    }
}
