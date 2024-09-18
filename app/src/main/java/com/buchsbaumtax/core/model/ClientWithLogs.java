package com.buchsbaumtax.core.model;

import java.util.List;

public class ClientWithLogs {
    private Client client;
    private List<Log> logs;

    public ClientWithLogs(Client client, List<Log> logs) {
        this.client = client;
        this.logs = logs;
    }

}
