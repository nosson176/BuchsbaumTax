package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateClient {
    static final Logger logger = LoggerFactory.getLogger(UpdateClient.class);
    public Client updateClient(int clientId, Client client) {
//        logger.info("client: {} client id {}", client,clientId);
        Client oldClient = Database.dao(ClientDAO.class).get(clientId);
        if (oldClient == null || clientId != client.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Database.dao(ClientDAO.class).update(client);
        return Database.dao(ClientDAO.class).get(clientId);
    }
}
