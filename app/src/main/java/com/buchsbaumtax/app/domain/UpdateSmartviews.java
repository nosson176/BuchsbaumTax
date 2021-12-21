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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateSmartviews {

    public void updateClients() {
        List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getAll();

        for (Smartview smartview : smartviews) {
            List<SmartviewLine> smartviewLines = smartview.getSmartviewLines();

            List<Client> clients;
            StringBuilder fromBuilder = new StringBuilder();
            StringBuilder whereBuilder = new StringBuilder();
            List<String> tables = new ArrayList<>();
            tables.add("clients");
            fromBuilder.append("FROM clients ");
            boolean first = true;

            if (smartviewLines == null || smartviewLines.isEmpty()) {
                smartview.setClientIds(new ArrayList<>());
            }
            else {
                List<String> fields = new ArrayList<>();
                for (SmartviewLine smartviewLine : smartviewLines) {
                    if (smartviewLine.getClassToJoin() != null && smartviewLine.getFieldToSearch() != null) {
                        String table = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, smartviewLine.getClassToJoin()) + "s";
                        String field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, smartviewLine.getFieldToSearch());
                        Object searchValue = parseSearchValue(smartviewLine);

                        if (!tables.contains(table)) {
                            if (table.equals("filings") || table.equals("tax_years")) {
                                fromBuilder.append("JOIN tax_years ON clients.id = tax_years.client_id JOIN filings ON tax_years.id = filings.tax_year_id ");
                                tables.add("tax_years");
                                tables.add("filings");
                            }
                            else {
                                fromBuilder.append(String.format("JOIN %s ON clients.id = %s.client_id ", table, table));
                                tables.add(table);
                            }
                        }

                        String tableField = table + field;
                        if (searchValue != null) {
                            if (first) {
                                whereBuilder.append("WHERE ");
                            }
                            else if (fields.contains(tableField)) {
                                whereBuilder.append("OR ");
                            }
                            else {
                                whereBuilder.append("AND ");
                            }
                            whereBuilder.append(String.format("(%s.%s %s '%s') ", table, field, smartviewLine.getOperator(), searchValue));
                            fields.add(tableField);
                            first = false;
                        }
                        else if (table.equals("tax_years")) {
                            String where = getTaxYearWhere(field, smartviewLine.getOperator(), smartviewLine.getSearchValue());
                            if (where != null) {
                                if (first) {
                                    whereBuilder.append("WHERE ");
                                }
                                else if (fields.contains(tableField)) {
                                    whereBuilder.append("OR ");
                                }
                                else {
                                    whereBuilder.append("AND ");
                                }
                                whereBuilder.append(where);
                                fields.add(tableField);
                                first = false;
                            }
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

    private String getTaxYearWhere(String field, String operator, String searchValue) {
        String[] components = field.split("_");
        List<String> taxYearFields = Arrays.asList("archived", "irs_history");
        if (field.contains("status")) {
            String filingType = "";
            String statusField = "";
            if (components.length >= 4 && components[2].equals("status")) {
                filingType = components[3];
                statusField = "status";
            }
            else if (components[1].equals("status")) {
                filingType = components[0];
                if (field.contains("detail")) {
                    statusField = "status_detail";
                }
                else {
                    statusField = "status";
                }
            }
            return String.format("(filings.filing_type = '%s' AND filings.%s = '%s') ", filingType, statusField, searchValue);
        }
        else if (field.equals("year_name")) {
            return String.format("(tax_years.year = '%s') ", searchValue);
        }
        else if (components.length >= 2 && field.contains("paid") || field.contains("owes") && (operator != null && searchValue != null)) {
            return String.format("(filings.filing_type = '%s' AND filings.%s %s %s) ", components[1], components[0], operator, searchValue);
        }
        else if (field.equals("extension_form")) {
            return String.format("(filings.filing_type = 'ext' AND filings.tax_form = '%s') ", searchValue);
        }
        else if (taxYearFields.contains(field)) {
            return String.format("(tax_years.%s = '%s') ", field, Boolean.parseBoolean(searchValue));
        }
        else if (field.equals("comment")) {
            return String.format("(filings.memo = '%s') ", searchValue);
        }
        else {
            return null;
        }
    }

    private Object parseSearchValue(SmartviewLine smartviewLine) {
        String type = smartviewLine.getType();
        if (type != null && smartviewLine.getSearchValue() != null) {
            Object searchValue = smartviewLine.getSearchValue().trim();
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
            return searchValue;
        }
        return null;
    }
}
