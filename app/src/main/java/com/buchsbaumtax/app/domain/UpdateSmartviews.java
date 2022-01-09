package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.job.UpdateSmartviewsJob;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
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
            List<SmartviewLine> smartviewLines = smartview.getSmartviewLines();
            Map<Integer, Set<Integer>> queries = new HashMap<>();

            for (SmartviewLine smartviewLine : smartviewLines) {
                String table = smartviewLine.getClassToJoin();
                String field = smartviewLine.getFieldToSearch();
                String searchValue = smartviewLine.getSearchValue();
                String operator = smartviewLine.getOperator();

                StringBuilder query = new StringBuilder();
                query.append("SELECT c.* FROM clients c ");
                if (table != null && table.equals("clients")) {
                    query.append(String.format("WHERE %s %s ", field, operator));
                }
                else {
                    query.append(String.format("JOIN %s t ON c.id = t.client_id WHERE t.%s %s ", table, field, operator));
                }


                if (smartviewLine.getType() != null && smartviewLine.getType().equals("String")) {
                    query.append(String.format("'%s'", searchValue));
                }
                else {
                    query.append(String.format("%s", searchValue));
                }

                Set<Integer> clientIds = new HashSet<>();
                try {
                    List<Client> clientList = Database.dao(ClientDAO.class).getFilteredWithFields(query.toString());
                    clientIds = clientList.stream().map(Client::getId).collect(Collectors.toSet());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                Integer queryNumber = smartviewLine.getQuery();
                if (queryNumber == null) {
                    queryNumber = 0;
                }

                Set<Integer> queryResults;
                if (queries.containsKey(queryNumber)) {
                    queryResults = queries.get(queryNumber);
                    queryResults.retainAll(clientIds);
                }
                else {
                    queryResults = clientIds;
                }
                queries.put(queryNumber, queryResults);
            }

            Set<Integer> finalClientIds = new HashSet<>();
            for (Set<Integer> clientIds : queries.values()) {
                finalClientIds.addAll(clientIds);
            }

            smartview.setClientIds(new ArrayList<>(finalClientIds));
            Database.dao(SmartviewDAO.class).updateSmartview(smartview);
        }

        Logger.info("UpdateSmartviews Job completed.");
    }
}
