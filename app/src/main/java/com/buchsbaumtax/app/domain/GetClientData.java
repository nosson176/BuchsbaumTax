package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.dao.*;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetClientData {
    private static final Logger logger = LoggerFactory.getLogger(GetClientData.class);

    public ClientData getByClient(User user, int clientId,boolean active) {
        Client client = Database.dao(ClientDAO.class).get(clientId,active);
//        logger.info("Client data retrieved successfully for ccccccc {}, {} ", client,active);
        List<TaxYear> taxYears = Database.dao(TaxYearDAO.class).getByClient(client.getId());
//        logger.info("Client 1111111111111111111111 {}: {}",taxYears);
        List<TaxYearData> taxYearData = taxYears.stream()
                .map(TaxYearData::new)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
//        logger.info("Client 22222222222222222222 {}: {}", taxYearData);
        Database.dao(ClientHistoryDAO.class).create(user.getId(), clientId);

//        logger.info("Client1111  {}: {}", clientId,taxYearData);
        ClientData clientData = new ClientData(client, taxYearData);
//        logger.info("Client  {}: {}", clientId, clientData);
//        List<IncomeBreakdown> incomeBreakdowns = clientData.getIncomeBreakdowns();
//        for (IncomeBreakdown breakdown : incomeBreakdowns) {
//            Double amountUSD = ConvertToUSD.convertToUSD(breakdown.getAmount(), breakdown.getCurrency(), breakdown.getYears());
//            breakdown.setAmountUSD(amountUSD);
//        }
//        List<FbarBreakdown> fbarBreakdowns = clientData.getFbarBreakdowns();
//        for (FbarBreakdown breakdown : fbarBreakdowns) {
//            Double amountUSD = ConvertToUSD.convertToUSD(breakdown.getAmount(), breakdown.getCurrency(), breakdown.getYears());
//            breakdown.setAmountUSD(amountUSD);
//        }
        Integer clientFlag = Database.dao(ClientFlagDAO.class).getFlagForUserClient(user.getId(), clientId);
        clientData.setFlag(clientFlag);
//        logger.info("Client data for clientId {}: {}", clientId, clientData);
//        logger.info("Client data for clientId {}: {}", clientId, clientData.getIncomeBreakdowns().size());
//        logger.info("Client data for clientId {}: {}", clientId, clientData.getFbarBreakdowns().size());
        return clientData;
    }

    public static List<ClientWithLogs> getClientAndLogs(Long clientId) {
//        logger.info("Fetching clients and logs data for client with ID {}", clientId);

        return fetchClientsWithLogs(clientId);
    }

    private static List<ClientWithLogs> fetchClientsWithLogs(Long clientId) {
        List<Client> clients;

        if (clientId == null) {
            // Fetch all clients with a limit of 10 if clientId is null
            boolean active = true;
            clients = Database.dao(ClientDAO.class)
                    .getAll(active)
                    .stream()
                    .limit(10)
                    .collect(Collectors.toList());
//            logger.info("Retrieved {} clients", clients.size());
        } else {
            // Fetch a specific client by clientId
            clients = Database.dao(ClientDAO.class).getClientById(clientId);
//            logger.info("Retrieved client with ID {}: {}", clientId, clients.size() == 1 ? clients.get(0) : "Not found");
        }

        List<ClientWithLogs> clientsWithLogs = new ArrayList<>();

        for (Client client : clients) {
            List<Log> logs = Database.dao(LogDAO.class).getForClient(client.getId());
//            logger.info("Retrieved {} logs for client with ID {}", logs.size(), client.getId());
            clientsWithLogs.add(new ClientWithLogs(client, logs));
        }

//        logger.info("Prepared clientsWithLogs data with {} entries", clientsWithLogs.size());
        return clientsWithLogs;
    }






}
