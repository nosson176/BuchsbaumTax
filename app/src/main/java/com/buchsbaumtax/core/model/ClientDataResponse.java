package com.buchsbaumtax.core.model;

import com.buchsbaumtax.app.dto.TaxYearData;

import java.util.List;

public class ClientDataResponse {
    private Client client;
    private List<Filing> filings;
    private List<Log> logs;

    // Constructor
    public ClientDataResponse(Client client, List<Filing> filings,  List<Log> logs) {
        this.client = client;
        this.filings = filings;
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

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
}

