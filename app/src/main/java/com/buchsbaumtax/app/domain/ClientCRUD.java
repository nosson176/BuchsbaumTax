package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.google.common.base.CaseFormat;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        List<SmartviewLine> smartviewLines = Database.dao(SmartviewDAO.class).getSmartviewLines(smartviewId);

        if (smartviewLines.isEmpty()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        List<Client> clients;
        StringBuilder fromBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        List<String> tables = new ArrayList<>();
        tables.add("clients");
        fromBuilder.append("FROM clients ");
        boolean first = true;

        for (SmartviewLine smartviewLine : smartviewLines) {
            if (smartviewLine.getType() != null) {
                if (first) {
                    whereBuilder.append("WHERE ");
                }
                else {
                    whereBuilder.append("AND ");
                }

                String table = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, smartviewLine.getClassToJoin()) + "s";
                String field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, smartviewLine.getFieldToSearch());
                Object searchValue = parseSearchValue(smartviewLine);

                if (!tables.contains(table)) {
                    if (table.equals("filings")) {
                        fromBuilder.append(String.format("JOIN tax_years ON clients.id = tax_years.client_id JOIN filings ON tax_years.id = filings.tax_year_id "));
                        tables.add("tax_years");
                    }
                    else {
                        fromBuilder.append(String.format("JOIN %s ON clients.id = %s.client_id ", table, table));
                    }
                    tables.add(table);
                }

                whereBuilder.append(String.format("%s.%s %s '%s'", table, field, smartviewLine.getOperator(), searchValue));
                first = false;
            }
        }

        String from = fromBuilder.toString();
        String where = whereBuilder.toString();
        clients = Database.dao(ClientDAO.class).getFiltered(from, where);
        return clients;
    }

    private Object parseSearchValue(SmartviewLine smartviewLine) {
        String type = smartviewLine.getType();
        Object searchValue = smartviewLine.getSearchValue().trim();
        if (type != null) {
            switch (type) {
                case "boolean":
                    searchValue = Boolean.parseBoolean((String)searchValue);
                    break;
                case "int":
                    searchValue = Integer.parseInt((String)searchValue);
                    break;
                case "Date":
                    try {
                        searchValue = new SimpleDateFormat("MM/dd/yyyy").parse((String)searchValue);
                    }
                    catch (ParseException e) {
                        if (((String)searchValue).equalsIgnoreCase("today")) {
                            searchValue = new Date();
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }

        return searchValue;

    }

    public List<Client> getFiltered(String q) {
        return Database.dao(ClientDAO.class).getFiltered(q + ":*");
    }

    public Client update(int clientId, Client client) {
        Database.dao(ClientDAO.class).update(client);
        return Database.dao(ClientDAO.class).get(clientId);
    }
}
