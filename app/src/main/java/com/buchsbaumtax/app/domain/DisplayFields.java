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
        TaxPersonal primary = getByCategory(taxPersonals, TaxPersonal.CATEGORY_PRIMARY);
        TaxPersonal secondary = getByCategory(taxPersonals, TaxPersonal.CATEGORY_SECONDARY);

        boolean primaryExists = primary != null && (primary.getInformal() != null || primary.getFirstName() != null);
        boolean secondaryExists = secondary != null && (secondary.getInformal() != null || secondary.getFirstName() != null);
        if (primaryExists && secondaryExists) {
            String primaryName = primary.getInformal() != null ? primary.getInformal() : primary.getFirstName();
            String secondaryName = secondary.getInformal() != null ? secondary.getInformal() : secondary.getFirstName();
            return primaryName + " - " + secondaryName;
        }
        else if (primaryExists) {
            return primary.getInformal() != null ? primary.getInformal() : primary.getFirstName();
        }
        return null;
    }

    public void setDisplayPhone(int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        List<Contact> contacts = Database.dao(ContactDAO.class).getForClient(clientId);
        String displayPhone = getDisplayPhone(contacts);
        client.setDisplayPhone(displayPhone);
        Database.dao(ClientDAO.class).update(client);
    }

    public String getDisplayPhone(List<Contact> contacts) {
        return contacts.stream()
                .findFirst()
                .map(Contact::getMainDetail)
                .orElse(null);
    }

    private TaxPersonal getByCategory(List<TaxPersonal> taxPersonals, String category) {
        return taxPersonals.stream()
                .filter(tp -> Objects.nonNull(tp.getCategory()) && tp.getCategory().equals(category))
                .findFirst()
                .orElse(null);
    }
}
