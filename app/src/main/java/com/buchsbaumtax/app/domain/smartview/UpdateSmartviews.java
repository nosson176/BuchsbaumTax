package com.buchsbaumtax.app.domain.smartview;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.dao.LogDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateSmartviews {
    private static final Logger logger = LoggerFactory.getLogger(UpdateSmartviews.class);

    public Map<Client, List<Filing>> updateSmartview(Smartview smartview) {
        List<SmartviewLine> smartviewLines = smartview.getSmartviewLines();
        logger.info("smartviewLines: {} ", smartviewLines);
        Set<Integer> finalClientIds = new HashSet<>();

        // Group each smartview by their unique query numbers
        Map<Integer, List<SmartviewLine>> linesGroupedByGroupNum = smartviewLines.stream()
                .collect(Collectors.groupingBy(SmartviewLine::getGroupNum));

        for (Integer key : linesGroupedByGroupNum.keySet()) {
            List<SmartviewLine> groupLines = linesGroupedByGroupNum.get(key);

            // Group each group's line by the tables they reference
            Map<String, List<SmartviewLine>> lineQueriesGroupedByTable = groupLines.stream()
                    .collect(Collectors.groupingBy(this::getTableName));

            Set<Integer> queryResults = null;
            for (String table : lineQueriesGroupedByTable.keySet()) {
                List<SmartviewLine> tableLines = lineQueriesGroupedByTable.get(table);

                Set<Integer> clientIds = getClientIds(tableLines);

                if (queryResults == null) {
                    queryResults = new HashSet<>(clientIds);
                } else {
                    queryResults.retainAll(clientIds);
                }
            }
            if (queryResults != null) {
                finalClientIds.addAll(queryResults);
            }
        }

        // Fetch the client data and filings based on the final client IDs
        Map<Client, List<Filing>> clientFilingsMap = new HashMap<>();
        if (!finalClientIds.isEmpty()) {
            List<Long> clientIdsAsLong = finalClientIds.stream()
                    .map(Integer::longValue)  // Convert each Integer to Long
                    .collect(Collectors.toList());
            boolean active;
            List<Client> clients = Database.dao(ClientDAO.class).getClientsByIds(clientIdsAsLong,active = false);
            for (Client client : clients) {
                List<Filing> filings = Database.dao(FilingDAO.class).getByClient(client.getId());
                clientFilingsMap.put(client, filings);
            }
        }

        // Update the smartview with the client IDs
        smartview.setClientIds(new ArrayList<>(finalClientIds));
        Database.dao(SmartviewDAO.class).updateSmartview(smartview);
//        logger.info("clientFilingsMap IS HERE!: {}", clientFilingsMap);
        return clientFilingsMap; // Return the map of client data + filings
    }

    private String getTableName(SmartviewLine smartviewLine) {
        if (smartviewLine.getTableName().equals("filings")) {
            return "tax_years";
        }
        return smartviewLine.getTableName();
    }

//    Set<Integer> getClientIds(List<SmartviewLine> smartviewLines) {
//        String table = smartviewLines.get(0).getTableName();
//        StringBuilder query = new StringBuilder();
//        query.append("SELECT DISTINCT(c.id) FROM clients c ");
//
//        if (table.equals("filings") || table.equals("tax_years")) {
//            query.append("JOIN filings f ON c.id = f.client_id JOIN tax_years ty ON c.id = ty.client_id ");
//        } else if (!table.equals("clients")) {
//            query.append(String.format("JOIN %s t ON c.id = t.client_id ", table));
//        }
//
//        boolean first = true;
//        for (SmartviewLine smartviewLine : smartviewLines) {
//            String field = smartviewLine.getField();
//            String searchValue = smartviewLine.getSearchValue();
//            String operator = smartviewLine.getOperator();
//
//            if (first) {
//                query.append("WHERE ");
//                first = false;
//            } else {
//                query.append("AND ");
//            }
//
//            switch (smartviewLine.getTableName()) {
//                case "clients":
//                    query.append(String.format("c.%s %s ", field, operator));
//                    break;
//                case "filings":
//                    if (field.equals("status") || field.equals("status_detail")) {
//                        query.append(String.format("f.%s->>'value' %s ", field, operator));
//                    } else {
//                        query.append(String.format("f.%s %s", field, operator));
//                    }
//                    break;
//                case "tax_years":
//                    query.append(String.format("ty.%s %s", field, operator));
//                    break;
//                default:
//                    query.append(String.format("t.%s %s ", field, operator));
//                    break;
//            }
//
//            if (smartviewLine.getType() != null && smartviewLine.getType().equals("String")) {
//                query.append(String.format("'%s' ", searchValue));
//            } else {
//                query.append(String.format("%s ", searchValue));
//            }
//        }
//
//        try {
//            logger.info("query: {}", query);
//            return Database.dao(ClientDAO.class).getClientIdsByQuery(query.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new HashSet<>();
//    }

    Set<Integer> getClientIds(List<SmartviewLine> smartviewLines) {
        String table = smartviewLines.get(0).getTableName();
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT(c.id) FROM clients c ");
logger.info("1111 : {}",query);
        // Handle joins for specific tables
        if (table.equals("filings") || table.equals("tax_years")) {
            query.append("JOIN filings f ON c.id = f.client_id JOIN tax_years ty ON c.id = ty.client_id ");
logger.info("2222 : {}",query);
        } else if (!table.equals("clients")) {
            query.append(String.format("JOIN %s t ON c.id = t.client_id ", table));
logger.info("3333 : {}",query);
        }

        boolean first = true;
        for (SmartviewLine smartviewLine : smartviewLines) {
            String field = smartviewLine.getField();
            String searchValue = smartviewLine.getSearchValue();
            String operator = smartviewLine.getOperator();

            if (first) {
                query.append("WHERE ");
                first = false;
            } else {
                query.append("AND ");
            }

            // Handle specific fields (alarm_time, status_date, log_date, date_of_birth, date_filed)
            if ("alarm_time".equals(field)  || "log_date".equals(field)) {
                long startOfDay;
                long endOfDay;

                if ("now()".equals(searchValue)) {
                    // Use current date if search value is "now()"
                    String now = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    startOfDay = convertToUnixMillis(now, true);
                    endOfDay = convertToUnixMillis(now, false);
                } else {
                    // Handle normal date range
                    startOfDay = convertToUnixMillis(searchValue, true);
                    endOfDay = convertToUnixMillis(searchValue, false);
                }

                // Handle case for using `<=` or `>=` separately
                if (operator.equals("<=")) {
                    query.append(String.format("t.%s <= %d ", field, endOfDay));
                } else if (operator.equals(">=")) {
                    query.append(String.format("t.%s >= %d ", field, startOfDay));
                } else if (operator.equals("=")) {
                    query.append(String.format("t.%s BETWEEN %d AND %d ", field, startOfDay, endOfDay));
                } else if ("<".equals(operator)) {
                    query.append(String.format("t.%s < %d ", field, startOfDay));
                } else if (">".equals(operator)) {
                    query.append(String.format("t.%s > %d ", field, endOfDay));
                } else if (operator.equals("!=")) {
                    // Exclude the date range
                    query.append(String.format("(t.%s < %d OR t.%s > %d) ", field, startOfDay, field, endOfDay));
                }
logger.info("4444 : {}",query);
            } else if ("date_of_birth".equals(field) ) {
                // These fields already use start of the day, so we can just compare them directly
                long dateMillis;

                if ("now()".equals(searchValue)) {
                    // Use current date if search value is "now()"
                    String now = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    dateMillis = convertToUnixMillis(now, true);
                } else {
                    // Convert to Unix time using start of the day
                    dateMillis = convertToUnixMillis(searchValue, true);
                }

                // Apply the operator
                if (operator.equals("=")) {
                    query.append(String.format("t.%s = %d ", field, dateMillis));
                } else if (operator.equals("<")) {
                    query.append(String.format("t.%s < %d ", field, dateMillis));
                } else if (operator.equals(">")) {
                    query.append(String.format("t.%s > %d ", field, dateMillis));
                } else if (operator.equals("!=")) {
                    query.append(String.format("t.%s != %d ", field, dateMillis));
                } else if (operator.equals(">=")) {
                    query.append(String.format("t.%s >= %d ", field, dateMillis));
                } else if (operator.equals("<=")) {
                    query.append(String.format("t.%s <= %d ", field, dateMillis));
                }

logger.info("5555 : {}",query);
            }
            else if ("date_filed".equals(field) ) {
                // These fields already use start of the day, so we can just compare them directly
                long dateMillisT;

                if ("now()".equals(searchValue)) {
                    // Use current date if search value is "now()"
                    String now = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    dateMillisT = convertToUnixMillis(now, true);
                } else {
                    // Convert to Unix time using start of the day
                    dateMillisT = convertToUnixMillis(searchValue, true);
                    logger.info("datemillisT:{}",dateMillisT);
                }

                // Apply the operator
                if (operator.equals("=")) {
                    query.append(String.format("f.%s = %d ", field, dateMillisT));
                } else if (operator.equals("<")) {
                    query.append(String.format("f.%s < %d ", field, dateMillisT));
                } else if (operator.equals(">")) {
                    query.append(String.format("f.%s > %d ", field, dateMillisT));
                } else if (operator.equals("!=")) {
                    query.append(String.format("f.%s != %d ", field, dateMillisT));
                } else if (operator.equals(">=")) {
                    query.append(String.format("f.%s >= %d ", field, dateMillisT));
                } else if (operator.equals("<=")) {
                    query.append(String.format("f.%s <= %d ", field, dateMillisT));
                }
logger.info("6666 : {}",query);
            }
            else {
                // Handle other fields normally based on the table
                switch (smartviewLine.getTableName()) {
                    case "clients":
                        query.append(String.format("c.%s %s ", field, operator));
logger.info("7777 : {}",query);
                        break;
                    case "filings":
                        if (field.equals("status") || field.equals("status_detail")) {
                            query.append(String.format("f.%s->>'value' %s ", field, operator));
logger.info("8888 : {}",query);
                        } else {
                            query.append(String.format("f.%s %s ", field, '='));
logger.info("9999 : {}",query);
                        }
                        break;
                    case "tax_years":
                        query.append(String.format("ty.%s %s ", field, operator));
logger.info("1010 : {}",query);
                        break;
                    default:
                        query.append(String.format("t.%s %s ", field, operator));
logger.info("1212 : {}",query);
                        break;
                }

                if (smartviewLine.getType() != null && smartviewLine.getType().equals("String")) {
                    query.append(String.format("'%s' ", searchValue));
logger.info("1313 : {}",query);
                } else {
                    query.append(String.format("%s ", searchValue));
logger.info("1414 : {}",query);
                }
            }
        }

        try {
            logger.info("query: {}", query);
            return Database.dao(ClientDAO.class).getClientIdsByQuery(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }


    // Convert MM/DD/YYYY to Unix timestamp (milliseconds)
    private long convertToUnixMillis(String dateStr, boolean startOfDay) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            ZoneId zone = ZoneId.systemDefault();

            if (startOfDay) {
                return date.atStartOfDay(zone).toInstant().toEpochMilli();
            } else {
                return date.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli();
            }
        } catch (Exception e) {
            logger.error("Error parsing date: {}", dateStr, e);
            return 0;
        }
    }




    public Map<Client, List<Filing>> getSmartviewResult(Smartview smartview, boolean active) {
        List<SmartviewLine> smartviewLines = smartview.getSmartviewLines();
        Set<Integer> finalClientIds = new HashSet<>();

        // Group each smartview by their unique query numbers
        Map<Integer, List<SmartviewLine>> linesGroupedByGroupNum = smartviewLines.stream()
                .collect(Collectors.groupingBy(SmartviewLine::getGroupNum));

        for (Integer key : linesGroupedByGroupNum.keySet()) {
            List<SmartviewLine> groupLines = linesGroupedByGroupNum.get(key);

            // Group each group's line by the tables they reference
            Map<String, List<SmartviewLine>> lineQueriesGroupedByTable = groupLines.stream()
                    .collect(Collectors.groupingBy(this::getTableName));

            Set<Integer> queryResults = null;
            for (String table : lineQueriesGroupedByTable.keySet()) {
                List<SmartviewLine> tableLines = lineQueriesGroupedByTable.get(table);

                Set<Integer> clientIds = getClientIds(tableLines);

                if (queryResults == null) {
                    queryResults = new HashSet<>(clientIds);
                } else {
                    queryResults.retainAll(clientIds);
                }
            }
            if (queryResults != null) {
                finalClientIds.addAll(queryResults);
            }
        }

        // Fetch the client data and filings based on the final client IDs
        Map<Client, List<Filing>> clientFilingsMap = new HashMap<>();
        if (!finalClientIds.isEmpty()) {
            List<Long> clientIdsAsLong = finalClientIds.stream()
                    .map(Integer::longValue)
                    .collect(Collectors.toList());
            List<Client> clients = Database.dao(ClientDAO.class).getClientsByIds(clientIdsAsLong,active);
            for (Client client : clients) {
                List<Filing> filings = Database.dao(FilingDAO.class).getByClient(client.getId());
                List<Log> logs = Database.dao(LogDAO.class).getForClient(client.getId());
                client.setFilings(filings);
                client.setLogs(logs);
                clientFilingsMap.put(client, filings);
            }
        }

        // Update the smartview with the client IDs
        smartview.setClientIds(new ArrayList<>(finalClientIds));
        Database.dao(SmartviewDAO.class).updateSmartview(smartview);

        return clientFilingsMap;
    }

}
