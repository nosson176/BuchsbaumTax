package com.buchsbaumtax.migration;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.SqlBatch;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientMigration extends Migration {

    public void uploadToDb(String filePath) {
        List<Map<String, ?>> clients = readClientCSV(filePath);

        ClientDAO clientDAO = handle.attach(ClientDAO.class);
        clientDAO.create(clients);
    }

    private List<Map<String, ?>> readClientCSV(String filePath) {
        List<Map<String, ?>> clients = new ArrayList<>();
        try {
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(rfc4180Parser);
            CSVReader reader = csvReaderBuilder.build();

            String[] next = reader.readNext();
            while (next != null) {
                Map<String, Object> map = new HashMap<>();
                int statusId = castToInt(next[1]);
                int owesStatusId = castToInt(next[2]);

                map.put("status", getValueFromId(statusId));
                map.put("owesStatus", getValueFromId(owesStatusId));
                map.put("periodical", next[3]);
                map.put("lastName", next[4]);
                map.put("archived", castToBoolean(next[5]));

                clients.add(map);
                next = reader.readNext();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return clients;
    }

    private String getValueFromId(int valueReference) {
        if (valueReference > 0) {
            return handle.select("SELECT value FROM value_lists WHERE id = ?", valueReference)
                    .mapTo(String.class)
                    .one();
        }
        else {
            return null;
        }
    }

    private interface ClientDAO {
        @SqlBatch("INSERT INTO clients (status, owes_status, periodical, last_name, archived) VALUES (:status, :owesStatus, :periodical, :lastName, :archived)")
        void create(@BindMap List<Map<String, ?>> clients);
    }
}
