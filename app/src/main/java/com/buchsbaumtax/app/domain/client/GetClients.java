package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.app.dto.ClientInfo;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ClientFlagDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Smartview;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class GetClients {
    ClientFlagDAO clientFlagDAO = Database.dao(ClientFlagDAO.class);

    public List<ClientInfo> getAllByUser(int userId) {
        return Database.dao(ClientDAO.class).getAllByUser(userId);
    }

    public List<Client> getAll() {
        return Database.dao(ClientDAO.class).getAll();
    }

    public List<ClientInfo> getForSmartview(int userId, int smartviewId) {
        Smartview smartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        if (smartview.getClientIds().isEmpty() || smartview.getClientIds() == null) {
            return new ArrayList<>();
        }
        return Database.dao(ClientDAO.class).getBulk(userId, smartview.getClientIds());
    }

    public List<ClientInfo> getForDefaultSearch(String q, int userId) {
        return Database.dao(ClientDAO.class).getFiltered(q, userId);
    }

    public List<ClientInfo> getForFieldSearch(String q, String field, int userId) {
        String[] fieldArray = field.split("::");
        if (fieldArray.length < 2) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        String table = fieldArray[0];
        String fieldName = fieldArray[1];
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT c.*, cf.flag FROM clients c JOIN client_flags cf ON c.id = cf.client_id ");
        if (!table.equals("clients")) {
            query.append(String.format("JOIN %s t ON c.id = t.client_id WHERE cf.user_id = %s AND t.%s ILIKE '%%%s%%'", table, userId, fieldName, q));
        }
        else {
            query.append(String.format("WHERE cf.user_id = %s AND %s ILIKE '%%%s%%' ", userId, fieldName, q));
        }
        query.append("ORDER BY c.last_name");
        String queryString = query.toString();
        return Database.dao(ClientDAO.class).getFilteredWithFields(queryString);
    }
}
