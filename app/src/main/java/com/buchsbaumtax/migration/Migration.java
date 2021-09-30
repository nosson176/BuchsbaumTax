package com.buchsbaumtax.migration;

import com.buchsbaumtax.core.model.Client;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Migration {
    private Handle handle;
    private Map<String, Integer> clientIds = new HashMap<>();
    private Map<String, String> statusMap = new HashMap<>();
    private Map<String, String> owesStatusMap = new HashMap<>();
    private Map<String, String> currenciesMap = new HashMap<>();

    private Migration() {
        handle = getHandle();
        setValues();
    }

    private Handle getHandle() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/buchsbaum", "postgres", "sifra123");
            Jdbi jdbi = Jdbi.create(connection);
            jdbi.installPlugin(new SqlObjectPlugin());
            return jdbi.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setValues() {
        List<String[]> statuses = parseCSV("C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\client_statuses.csv");
        for (String[] row : statuses) {
            statusMap.put(row[0], row[1]);
        }

        List<String[]> owesStatuses = parseCSV("C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\owes_statuses.csv");
        for (String[] row : owesStatuses) {
            owesStatusMap.put(row[0], row[1]);
        }

        List<String[]> currencies = parseCSV("C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\currencies.csv");
        for (String[] row : currencies) {
            currenciesMap.put(row[0], row[1]);
        }
    }

    private List<String[]> parseCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try {
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(rfc4180Parser);
            CSVReader reader = csvReaderBuilder.build();

            String[] next = reader.readNext();
            while (next != null) {
                data.add(next);
                next = reader.readNext();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void csvToClient(List<String[]> clients) {
        for (String[] row : clients) {
            Map<String, Object> map = new HashMap<>();

            map.put("status", statusMap.get(row[1]));
            map.put("owesStatus", owesStatusMap.get(row[2]));
            map.put("periodical", row[3]);
            map.put("lastName", row[4]);
            map.put("archived", castToBoolean(row[5]));

            ClientDAO clientDAO = handle.attach(ClientDAO.class);
            int clientId = clientDAO.create(map);
            clientIds.put(row[0], clientId);
        }
    }

    private void csvToContact(List<String[]> contacts) {
        for (String[] row : contacts) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[1]));
            map.put("contactType", row[2]);
            map.put("memo", row[3]);
            map.put("mainDetail", row[4]);
            map.put("secondaryDetail", row[5]);
            map.put("state", row[6]);
            map.put("zip", castToInt(row[7]));

            ContactDAO contactDAO = handle.attach(ContactDAO.class);
            contactDAO.create(map);
        }
    }

    private void csvToExchangeRate(List<String[]> exchangeRates) {
        for (String[] row : exchangeRates) {
            Map<String, Object> map = new HashMap<>();

            map.put("currency", row[0]);
            map.put("year", row[1]);
            map.put("show", castToBoolean(row[2]));
            map.put("rate", castToDouble(row[3]));

            ExchangeRateDAO exchangeRateDAO = handle.attach(ExchangeRateDAO.class);
            exchangeRateDAO.create(map);
        }
    }

    private void csvToUser(List<String[]> users) {
        for (String[] row : users) {
            Map<String, Object> map = new HashMap<>();
            String[] nameList = row[1].split("\\s+");

            map.put("firstName", nameList[0]);
            if (nameList.length > 1) {
                map.put("lastName", nameList[1]);
            }
            else {
                map.put("lastName", null);
            }
            map.put("username", row[2]);
            map.put("sendLoginNotifications", castToBoolean(row[3]));
            map.put("notifyOfLogins", castToBoolean(row[4]));

            int secondsInDay = castToInt(row[5]);
            if (secondsInDay == -1) {
                map.put("secondsInDay", null);
            }
            else {
                map.put("secondsInDay", castToInt(row[5]));
            }
            map.put("allowTexting", castToBoolean(row[6]));
            map.put("selectable", castToBoolean(row[7]));
            map.put("userType", row[8]);

            UserDAO userDAO = handle.attach(UserDAO.class);
            userDAO.create(map);
        }
    }

    private boolean castToBoolean(String str) {
        return str.equals("1") || str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes");
    }

    private int castToInt(String str) {
        try {
            return Integer.parseInt(str);
        }
        catch (Exception e) {
            return -1;
        }
    }

    private double castToDouble(String str) {
        try {
            return Double.parseDouble(str);
        }
        catch (Exception e) {
            return -1.0;
        }
    }

    private interface ClientDAO {
        @RegisterFieldMapper(Client.class)
        @SqlUpdate("INSERT INTO clients (status, owes_status, periodical, last_name, archived) VALUES (:status, :owesStatus, :periodical, :lastName, :archived)")
        @GetGeneratedKeys
        int create(@BindMap Map<String, ?> client);
    }

    private interface ContactDAO {
        @SqlUpdate("INSERT INTO contacts (contact_type, memo, main_detail, secondary_detail, state, zip, client_id) VALUES (:contactType, :memo, :mainDetail, :secondaryDetail, :state, :zip, :clientId)")
        void create(@BindMap Map<String, ?> contact);
    }

    private interface ExchangeRateDAO {
        @SqlUpdate("INSERT INTO exchange_rates (rate, show, currency, year) VALUES (:rate, :show, :currency, :year)")
        void create(@BindMap Map<String, ?> exchangeRate);
    }

    private interface UserDAO {
        @SqlUpdate("INSERT INTO users (first_name, last_name, username, send_login_notifications, notify_of_logins, seconds_in_day, allow_texting, selectable, user_type, crypted_password, password_salt, persistence_token, single_access_token, perishable_token) VALUES (:firstName, :lastName, :username, :sendLoginNotifications, :notifyOfLogins, :secondsInDay, :allowTexting, :selectable, :userType, '400$8$3e$00af41f95b8ba344$68681f3a80dc86061665b0558bb197bdd9d2b97379052815036081cf5a482a9a', '1a576b4c2c9cdada50daffbebcfc39830fb7c796e29af75c2c5c58f0ce3f28e8763c4bf1f2130b192fb6e5737df3a99bd1ae270bf4b48c3efe55e6eb8370eb84', '3b974c03acb2a86bb2c393cf027896d20ba913e205b2cca38b4b2bc3c95fcdb08496936f98b1cf5a5742ae7ddf67cebea722f29fd2b04bdef22a5d24a1e49723', 'cCGRKm4YOX9XxvckAbwi', 'DiOuket1AJEeoNdM-diY')")
        void create(@BindMap Map<String, ?> user);
    }

    public static void main(String[] args) {
        Migration migration = new Migration();

        System.out.println("Uploading clients...");
        List<String[]> clients = migration.parseCSV("C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\clients.csv");
        migration.csvToClient(clients);
        System.out.println("Clients uploaded");

        System.out.println("Uploading contacts...");
        List<String[]> contacts = migration.parseCSV("C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\contacts.csv");
        migration.csvToContact(contacts);
        System.out.println("Contacts completed.");

        System.out.println("Uploading exchange rates...");
        List<String[]> exchangeRates = migration.parseCSV("C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\exchange_rates.csv");
        migration.csvToExchangeRate(exchangeRates);
        System.out.println("Exchange rates completed.");

        System.out.println("Uploading users...");
        List<String[]> users = migration.parseCSV("C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\users.csv");
        migration.csvToUser(users);
        System.out.println("Users completed.");
    }
}
