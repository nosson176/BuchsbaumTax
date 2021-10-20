package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.domain.taxyear.GetClientData;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.ClientData;
import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Database;

import java.util.List;
import java.util.stream.Collectors;

public class ClientCRUD {
    public Client create(Client client) {
        int id = Database.dao(ClientDAO.class).create(client);
        return Database.dao(ClientDAO.class).get(id);
    }

    public BaseResponse delete(int clientId) {
        Database.dao(ClientDAO.class).delete(clientId);
        return new BaseResponse(true);
    }

    public List<Client> getAll() {
        return Database.dao(ClientDAO.class).getAll();
    }

    public Client get(int clientId) {
        return Database.dao(ClientDAO.class).get(clientId);
    }

    public List<Client> getFiltered(String q) {
        List<Client> clients = Database.dao(ClientDAO.class).getAll();
//        clients = clients.stream().filter(cd -> (cd.getStatus() != null && cd.getStatus().toLowerCase().contains(q.toLowerCase())) ||
//                (cd.getOwesStatus() != null && cd.getOwesStatus().toLowerCase().contains(q.toLowerCase())) ||
//                (cd.getPeriodical() != null && cd.getPeriodical().toLowerCase().contains(q.toLowerCase())) ||
//                (cd.getLastName() != null && cd.getLastName().toLowerCase().contains(q.toLowerCase())) ||
//                (cd.getDisplayName() != null && cd.getDisplayName().toLowerCase().contains(q.toLowerCase())) ||
//                (cd.getDisplayPhone() != null && cd.getDisplayPhone().toLowerCase().contains(q.toLowerCase())) //||
//        ).collect(Collectors.toList());
        List<ClientData> clientData = clients.stream().map(c -> new GetClientData().getByClient(c.getId())).collect(Collectors.toList());
//                cd.getTaxYears().stream().anyMatch(ty -> ty.getYearName().contains(q) ||
//                        ty.getFilings().stream().anyMatch(f -> f.getCurrency().contains(q) ||
//                                f.getMemo().contains(q) ||
//                                f.getState().contains(q) ||
//                                f.getFilingType().contains(q) ||
//                                f.getFileType().contains(q) ||
//                                f.getStatusDetail().contains(q) ||
//                                f.getStatus().contains(q) ||
//                                f.getTaxForm().contains(q))
//                ) ||
//                cd.getFbarBreakdowns().stream().anyMatch(fb -> fb.getDepend().contains(q) ||
//                        fb.getDescription().contains(q) ||
//                        fb.getDocuments().contains(q) ||
//                        fb.getCurrency().contains(q) ||
//                        fb.getPart().contains(q) ||
//                        fb.getTaxType().contains(q) ||
//                        fb.getTaxGroup().contains(q) ||
//                        fb.getCategory().contains(q)) ||
//                cd.getIncomeBreakdowns().stream().anyMatch(ib -> ib.getDepend().contains(q) ||
//                        ib.getDescription().contains(q) ||
//                        ib.getDocuments().contains(q) ||
//                        ib.getCurrency().contains(q) ||
//                        ib.getJob().contains(q) ||
//                        ib.getTaxType().contains(q) ||
//                        ib.getTaxGroup().contains(q) ||
//                        ib.getCategory().contains(q)) ||
//                cd.getLogs().stream().anyMatch(l -> l.getAlarmTime().contains(q) ||
//                        l.getNote().contains(q)) ||
//                cd.getContacts().stream().anyMatch(co -> co.getZip().contains(q) ||
//                        co.getState().contains(q) ||
//                        co.getSecondaryDetail().contains(q) ||
//                        co.getMainDetail().contains(q) ||
//                        co.getMemo().contains(q) ||
//                        co.getContactType().contains(q)) ||
//                cd.getTaxPersonals().stream().anyMatch(tp -> tp.getCategory().contains(q) ||
//                        tp.getFirstName().contains(q) ||
//                        tp.getMiddleInitial().contains(q) ||
//                        tp.getLastName().contains(q) ||
//                        tp.getSsn().contains(q) ||
//                        tp.getInformal().contains(q) ||
//                        tp.getRelation().contains(q) ||
//                        tp.getLanguage().contains(q))
//        ).collect(Collectors.toList());
        return clients;
    }

    public Client update(int clientId, Client client) {
        Database.dao(ClientDAO.class).update(client);
        return Database.dao(ClientDAO.class).get(clientId);
    }
}
