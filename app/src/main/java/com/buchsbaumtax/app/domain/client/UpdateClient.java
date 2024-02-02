package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UpdateClient {

    public Client updateClient(int clientId, Client client) {
        Client oldClient = Database.dao(ClientDAO.class).get(clientId);
        if (oldClient == null || clientId != client.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(ClientDAO.class).update(client);
        return Database.dao(ClientDAO.class).get(clientId);
    }
}
