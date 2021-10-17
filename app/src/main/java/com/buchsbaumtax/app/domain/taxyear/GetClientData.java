package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;

import java.util.List;
import java.util.stream.Collectors;

public class GetClientData {

    public ClientData getByClient(int clientId) {
        Client client = Database.dao(ClientDAO.class).get(clientId);
        List<TaxYear> taxYears = Database.dao(TaxYearDAO.class).getByClient(client.getId());
        List<TaxYearData> taxYearData = taxYears.stream().map(TaxYearData::new).collect(Collectors.toList());
        return new ClientData(client, taxYearData);
    }
}