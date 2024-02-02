package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Database;

public class CreateClient {

    public Client createClient(Client client) {
        int id = Database.dao(ClientDAO.class).create(client);
        return Database.dao(ClientDAO.class).get(id);
    }
}
