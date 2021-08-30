package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Database;

public class GetClientData {

    public ClientData getByClient(int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        return new ClientData(client);
    }
}
