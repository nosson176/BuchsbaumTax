package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.google.common.base.CaseFormat;
import com.sifradigital.framework.db.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateSmartviews {

    public void updateClients() {
        List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getAll();

        for (Smartview smartview : smartviews) {
            List<SmartviewLine> smartviewLines = Database.dao(SmartviewDAO.class).getSmartviewLines(smartview.getId());

            List<Client> clients;
            StringBuilder fromBuilder = new StringBuilder();
            StringBuilder whereBuilder = new StringBuilder();
            List<String> tables = new ArrayList<>();
            tables.add("clients");
            fromBuilder.append("FROM clients ");
            boolean first = true;

            if (smartviewLines == null || smartviewLines.isEmpty()) {
                smartview.setClientIds(null);
            }
            else {
                for (SmartviewLine smartviewLine : smartviewLines) {
                    if (smartviewLine.getType() != null) {

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

                        if (searchValue != null) {
                            if (first) {
                                whereBuilder.append("WHERE ");
                            }
                            else {
                                whereBuilder.append("AND ");
                            }
                            whereBuilder.append(String.format("%s.%s %s '%s'", table, field, smartviewLine.getOperator(), searchValue));
                            first = false;
                        }
                    }
                }

                String from = fromBuilder.toString();
                String where = whereBuilder.toString();
                clients = Database.dao(ClientDAO.class).getFiltered(from, where);
                List<Integer> clientIds = clients.stream().map(Client::getId).collect(Collectors.toList());
                smartview.setClientIds(clientIds);
            }
            Database.dao(SmartviewDAO.class).updateSmartview(smartview);
        }
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
                            return null;
                        }
                    }
                    break;
            }
        }

        return searchValue;

    }
}
