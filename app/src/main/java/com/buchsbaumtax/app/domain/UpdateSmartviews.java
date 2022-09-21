package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.job.UpdateSmartviewsJob;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class UpdateSmartviews {
    private static final Logger Logger = LoggerFactory.getLogger(UpdateSmartviewsJob.class);

    public void updateClients() {
        Logger.info("Starting Update Smartviews Job...");

        List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getAll();

        for (Smartview smartview : smartviews) {
            updateSmartview(smartview);
        }

        Logger.info("UpdateSmartviews Job completed.");
    }

    public void updateSmartview(Smartview smartview) {
        List<SmartviewLine> smartviewLines = smartview.getSmartviewLines();
        Set<Integer> finalClientIds = new HashSet<>();

        // Group each smartview's by their unique query numbers
        Map<Integer, List<SmartviewLine>> linesGroupedByGroupNum = smartviewLines.stream().collect(Collectors.groupingBy(SmartviewLine::getGroupNum));

        for (Integer key : linesGroupedByGroupNum.keySet()) {
            List<SmartviewLine> groupLines = linesGroupedByGroupNum.get(key);

            // Group each group's line by the tables they reference
            Map<String, List<SmartviewLine>> lineQueriesGroupedByTable = groupLines.stream().collect(Collectors.groupingBy(this::getTableName));

            Set<Integer> queryResults = null;
            for (String table : lineQueriesGroupedByTable.keySet()) {
                List<SmartviewLine> tableLines = lineQueriesGroupedByTable.get(table);

                Set<Integer> clientIds = getClientIds(tableLines);

                if (queryResults == null) {
                    queryResults = new HashSet<>(clientIds);
                }
                else {
                    queryResults.retainAll(clientIds);
                }
            }
            if (queryResults != null) {
                finalClientIds.addAll(queryResults);
            }
        }
        smartview.setClientIds(new ArrayList<>(finalClientIds));
        Database.dao(SmartviewDAO.class).updateSmartview(smartview);
    }

    private String getTableName(SmartviewLine smartviewLine) {
        if (smartviewLine.getTableName().equals("filings")) {
            return "tax_years";
        }
        return smartviewLine.getTableName();
    }

    private Set<Integer> getClientIds(List<SmartviewLine> smartviewLines) {
        String table = smartviewLines.get(0).getTableName();
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT(c.id) FROM clients c ");

        if (table.equals("filings") || table.equals("tax_years")) {
            query.append("JOIN filings f ON c.id = f.client_id JOIN tax_years ty ON c.id = ty.client_id ");
        }
        else if (!table.equals("clients")) {
            query.append(String.format("JOIN %s t ON c.id = t.client_id ", table));
        }

        boolean first = true;
        for (SmartviewLine smartviewLine : smartviewLines) {
            String field = smartviewLine.getField();
            String searchValue = smartviewLine.getSearchValue();
            String operator = smartviewLine.getOperator();

            if (first) {
                query.append("WHERE ");
                first = false;
            }
            else {
                query.append("AND ");
            }


            switch (smartviewLine.getTableName()) {
                case "clients":
                    query.append(String.format("c.%s %s ", field, operator));
                    break;
                case "filings":
                    query.append(String.format("f.%s %s", field, operator));
                    break;
                case "tax_years":
                    query.append(String.format("ty.%s %s", field, operator));
                    break;
                default:
                    query.append(String.format("t.%s %s ", field, operator));
                    break;
            }

            if (smartviewLine.getType() != null && smartviewLine.getType().equals("String")) {
                query.append(String.format("'%s' ", searchValue));
            }
            else {
                query.append(String.format("%s ", searchValue));
            }
        }

        try {
            return Database.dao(ClientDAO.class).getClientIdsByQuery(query.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }
}
