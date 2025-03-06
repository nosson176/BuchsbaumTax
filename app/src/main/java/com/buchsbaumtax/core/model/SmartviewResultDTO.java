package com.buchsbaumtax.core.model;

import java.util.List;
import com.buchsbaumtax.core.model.Client;

public class SmartviewResultDTO {
    private List<Client> clients;
    private List<Integer> clientIds;

    public SmartviewResultDTO(List<Client> clients, List<Integer> clientIds) {
        this.clients = clients;
        this.clientIds = clientIds;
    }

    // Getters and setters
    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Integer> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Integer> clientIds) {
        this.clientIds = clientIds;
    }

    @Override
    public String toString() {
        return "SmartviewResultDTO{" +
                "clients=" + (clients != null ? clients.size() : "null") + " items" +
                ", clientIds=" + (clientIds != null ? clientIds.size() : "null") + " items" +
                '}';
    }
}