package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.model.Filing;

import java.util.List;

public class FilingUpdateRequest {
    private int clientId;
    private String oldContectDelivary;
    private String newContectDelivary;
    private List<Filing> updates;


    // Getters
    public int getClientId() {
        return clientId;
    }

    public String getOldContectDelivary() {
        return oldContectDelivary;
    }

    public String getNewContectDelivary() {
        return newContectDelivary;
    }

    public List<Filing> getUpdates() {
        return updates;
    }



    // Setters
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setOldContectDelivary(String oldContectDelivary) {
        this.oldContectDelivary = oldContectDelivary;
    }

    public void setNewContectDelivary(String newContectDelivary) {
        this.newContectDelivary = newContectDelivary;
    }

    public void setUpdates(List<Filing> updates) {
        this.updates = updates;
    }
}

