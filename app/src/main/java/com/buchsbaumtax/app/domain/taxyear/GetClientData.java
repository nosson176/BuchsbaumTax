package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.app.domain.ConvertToUSD;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.ClientHistoryDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GetClientData {

    public ClientData getByClient(User user, int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        List<TaxYear> taxYears = Database.dao(TaxYearDAO.class).getByClient(client.getId());
        List<TaxYearData> taxYearData = taxYears.stream().map(TaxYearData::new).collect(Collectors.toList()).stream()
                .sorted((ty1, ty2) -> Integer.compare(NumberUtils.toInt(ty2.getYear(), -1), NumberUtils.toInt(ty1.getYear(), -1)))
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
        return clientData;
    }
}
