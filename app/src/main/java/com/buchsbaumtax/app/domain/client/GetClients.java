package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.app.dto.ClientInfo;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.util.NaturalOrderComparator;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetClients {
    ClientDAO clientDAO = Database.dao(ClientDAO.class);
    SmartviewDAO smartviewDAO = Database.dao(SmartviewDAO.class);

    public List<ClientInfo> getAllByUser(int userId) {
        return clientDAO.getAll().stream().map(c -> new ClientInfo(c, userId)).collect(Collectors.toList());
    }

    public List<Client> getAll() {
        List<Client> clients = clientDAO.getAll();
        sort(clients);
        return clients;
    }

    public List<ClientInfo> getForSmartview(int userId, int smartviewId) {
        Smartview smartview = smartviewDAO.get(smartviewId);
        if (smartview.getClientIds().isEmpty() || smartview.getClientIds() == null) {
            return new ArrayList<>();
        }
        List<Client> clients = clientDAO.getBulk(smartview.getClientIds());
        sort(clients);
        return clients.stream().map(c -> new ClientInfo(c, userId)).collect(Collectors.toList());
    }

    public List<ClientInfo> getForDefaultSearch(String q, int userId) {
        List<Client> clients = clientDAO.getFiltered(q);
        sort(clients);
        return clients.stream().map(c -> new ClientInfo(c, userId)).collect(Collectors.toList());
    }

    public List<ClientInfo> getForFieldSearch(String q, String field, int userId) {
        String[] fieldArray = field.split("::");
        if (fieldArray.length < 2) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        String table = fieldArray[0];
        String fieldName = fieldArray[1];
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT c.id as c_id, c.status as c_status, c.owes_status as c_owes_status, c.periodical as c_periodical, c.last_name as c_last_name, c.archived as c_archived, c.display_name as c_display_name, c.display_phone as c_display_phone, c.created as c_created, c.updated as c_updated, cf.* FROM clients c JOIN client_flags cf ON c.id = cf.client_id ");
        if (!table.equals("clients")) {
            query.append(String.format("JOIN %s t ON c.id = t.client_id WHERE cf.user_id = %s AND t.%s ILIKE '%%%s%%'", table, userId, fieldName, q));
        }
        else {
            query.append(String.format("WHERE cf.user_id = %s AND %s ILIKE '%%%s%%' ", userId, fieldName, q));
        }
        query.append("ORDER BY c.last_name");
        String queryString = query.toString();
        List<Client> clients = Database.dao(ClientDAO.class).getFilteredWithFields(queryString);
        sort(clients);
        return clients.stream().map(c -> new ClientInfo(c, userId)).collect(Collectors.toList());
    }

    private void sort(List<Client> clients) {
        clients.sort(Comparator.comparing(Client::getLastName, new NaturalOrderComparator()));
    }
}
