package com.buchsbaumtax.core.dao.mapper;

import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.ClientFlag;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Map;

public class ClientReducer implements LinkedHashMapRowReducer<Integer, Client> {

    @Override
    public void accumulate(Map<Integer, Client> container, RowView rowView) {
        Client client = container.computeIfAbsent(rowView.getColumn("id", Integer.class),
                id -> rowView.getRow(Client.class));

        if (rowView.getColumn("flag", Integer.class) != null) {
            client.getFlags().add(rowView.getRow(ClientFlag.class));
        }
    }
}
