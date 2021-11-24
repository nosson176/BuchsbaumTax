package com.buchsbaumtax.app.dto;

import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.sifradigital.framework.db.Database;

import java.util.List;

public class SmartviewData {
    private int id;
    private String userName;
    private long userId;
    private String name;
    private long sortNumber;
    private boolean archived;
    private List<Integer> clientIds;
    private List<SmartviewLine> smartviewLines;

    public SmartviewData(Smartview smartview) {
        this.id = smartview.getId();
        this.userName = smartview.getUserName();
        this.userId = smartview.getUserId();
        this.name = smartview.getName();
        this.sortNumber = smartview.getSortNumber();
        this.archived = smartview.getArchived();
        this.clientIds = smartview.getClientIds();
        this.smartviewLines = Database.dao(SmartviewDAO.class).getSmartviewLines(smartview.getId());
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public long getSortNumber() {
        return sortNumber;
    }

    public boolean isArchived() {
        return archived;
    }

    public List<Integer> getClientIds() {
        return clientIds;
    }

    public List<SmartviewLine> getSmartviewLines() {
        return smartviewLines;
    }
}
