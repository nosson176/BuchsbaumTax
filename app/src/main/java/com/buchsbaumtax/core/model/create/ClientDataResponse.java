package com.buchsbaumtax.core.model.create;

import com.buchsbaumtax.app.dto.TaxYearData;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.Log;

import java.util.List;

public class ClientDataResponse {
    private Client client;
    private List<Filing> filings;
    private List<TaxYearData> taxYearData;
    private List<Log> logs;

    // Constructor
    public ClientDataResponse(Client client, List<Filing> filings, List<TaxYearData> taxYearData, List<Log> logs) {
        this.client = client;
        this.filings = filings;
        this.taxYearData = taxYearData;
        this.logs = logs;
    }

    // Getters and setters
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Filing> getFilings() {
        return filings;
    }

    public void setFilings(List<Filing> filings) {
        this.filings = filings;
    }

    public List<TaxYearData> getTaxYearData() {
        return taxYearData;
    }

    public void setTaxYearData(List<TaxYearData> taxYearData) {
        this.taxYearData = taxYearData;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
}
