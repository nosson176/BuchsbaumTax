package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Smartview;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class ClientCRUD {
    public Client create(Client client) {
        int id = Database.dao(ClientDAO.class).create(client);
        return Database.dao(ClientDAO.class).get(id);
    }

    public List<Client> getAll() {
        return Database.dao(ClientDAO.class).getAll();
    }

    public Client get(int clientId) {
        return Database.dao(ClientDAO.class).get(clientId);
    }

    public List<Client> getFiltered(int smartviewId) {
        Smartview smartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        return Database.dao(ClientDAO.class).getBulk(smartview.getClientIds());
    }

    public List<Client> getFiltered(String q) {
        return Database.dao(ClientDAO.class).getFiltered(q + ":*");
    }

    public Client update(int clientId, Client client) {
        Database.dao(ClientDAO.class).update(client);
        return Database.dao(ClientDAO.class).get(clientId);
    }
}
