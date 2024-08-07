package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ClientFlagDAO;
import com.buchsbaumtax.core.dao.ClientHistoryDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetClientData {
    private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

    public ClientData getByClient(User user, int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        logger.info("Client data retrieved successfully for ccccccc {}: {}", client);
        List<TaxYear> taxYears = Database.dao(TaxYearDAO.class).getByClient(client.getId());
        List<TaxYearData> taxYearData = taxYears.stream()
                .map(TaxYearData::new)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Database.dao(ClientHistoryDAO.class).create(user.getId(), clientId);

        ClientData clientData = new ClientData(client, taxYearData);
        List<IncomeBreakdown> incomeBreakdowns = clientData.getIncomeBreakdowns();
        for (IncomeBreakdown breakdown : incomeBreakdowns) {
            Double amountUSD = ConvertToUSD.convertToUSD(breakdown.getAmount(), breakdown.getCurrency(), breakdown.getYears());
            breakdown.setAmountUSD(amountUSD);
        }
        List<FbarBreakdown> fbarBreakdowns = clientData.getFbarBreakdowns();
        for (FbarBreakdown breakdown : fbarBreakdowns) {
            Double amountUSD = ConvertToUSD.convertToUSD(breakdown.getAmount(), breakdown.getCurrency(), breakdown.getYears());
            breakdown.setAmountUSD(amountUSD);
        }
        Integer clientFlag = Database.dao(ClientFlagDAO.class).getFlagForUserClient(user.getId(), clientId);
        clientData.setFlag(clientFlag);
        logger.info("Client data for clientId {}: {}", clientId, clientData.getContacts().size());
        logger.info("Client data for clientId {}: {}", clientId, clientData.getIncomeBreakdowns().size());
        logger.info("Client data for clientId {}: {}", clientId, clientData.getFbarBreakdowns().size());
        return clientData;
    }
}
