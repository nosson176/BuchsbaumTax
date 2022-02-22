package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Smartview;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class GetClients {

    public List<Client> getAll() {
        return Database.dao(ClientDAO.class).getAll();
    }

    public List<Client> getForSmartview(int smartviewId) {
        Smartview smartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        return Database.dao(ClientDAO.class).getBulk(smartview.getClientIds());
    }

    public List<Client> getForDefaultSearch(String q) {
        return Database.dao(ClientDAO.class).getFiltered(q);
    }

    public List<Client> getForFieldSearch(String q, String field) {
        String[] fieldArray = field.split("::");
        if (fieldArray.length < 2) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        String table = fieldArray[0];
        String fieldName = fieldArray[1];
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT c.* FROM clients c ");
        if (!table.equals("clients")) {
            query.append(String.format("JOIN %s t ON c.id = t.client_id WHERE t.%s ILIKE '%%%s%%'", table, fieldName, q));
        }
        else {
            query.append(String.format("WHERE %s ILIKE '%%%s%%'", fieldName, q));
        }
        query.append("ORDER BY c.last_name");
        String queryString = query.toString();
        return Database.dao(ClientDAO.class).getFilteredWithFields(queryString);
    }
}
