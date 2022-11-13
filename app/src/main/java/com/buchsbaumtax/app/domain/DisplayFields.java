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

        boolean primaryExists = primary != null && (fieldExists(primary.getInformal()) || fieldExists(primary.getFirstName()));
        boolean secondaryExists = secondary != null && (fieldExists(secondary.getInformal()) || fieldExists(secondary.getFirstName()));
        if (primaryExists && secondaryExists) {
            String primaryName = fieldExists(primary.getInformal()) ? primary.getInformal() : primary.getFirstName();
            String secondaryName = fieldExists(secondary.getInformal()) ? secondary.getInformal() : secondary.getFirstName();
            return primaryName + " - " + secondaryName;
        }
        else if (primaryExists) {
            return fieldExists(primary.getInformal()) ? primary.getInformal() : primary.getFirstName();
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
                .filter(c -> c.getMainDetail() != null && !c.getMainDetail().isEmpty())
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

    private boolean fieldExists(String field) {
        return field != null && !field.isEmpty();
    }
}
