package com.buchsbaumtax.core.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SmartviewResult {
    private Map<Client, List<Filing>> clientFilings;
    private Set<Integer> clientIds;

    public SmartviewResult(Map<Client, List<Filing>> clientFilings, Set<Integer> clientIds) {
        this.clientFilings = clientFilings;
        this.clientIds = clientIds;
    }

    public Map<Client, List<Filing>> getClientFilings() {
        return clientFilings;
    }

    public Set<Integer> getClientIds() {
        return clientIds;
    }
}

