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

public class UserMigration extends Migration {


    public void uploadToDb(String filePath) {
        List<Map<String, ?>> users = readUserCSV(filePath);

        UserDAO userDAO = handle.attach(UserDAO.class);
        userDAO.create(users);
    }

    private List<Map<String, ?>> readUserCSV(String filePath) {
        List<Map<String, ?>> users = new ArrayList<>();
        try {
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(rfc4180Parser);
            CSVReader reader = csvReaderBuilder.build();

            String[] next = reader.readNext();
            while (next != null) {
                Map<String, Object> map = new HashMap<>();
                String[] nameList = next[1].split("\\s+");

                map.put("firstName", nameList[0]);
                if (nameList.length > 1) {
                    map.put("lastName", nameList[1]);
                }
                else {
                    map.put("lastName", null);
                }
                map.put("username", next[2]);
                map.put("sendLoginNotifications", castToBoolean(next[3]));
                map.put("notifyOfLogins", castToBoolean(next[4]));

                int secondsInDay = castToInt(next[5]);
                if (secondsInDay == -1) {
                    map.put("secondsInDay", null);
                }
                else {
                    map.put("secondsInDay", castToInt(next[5]));
                }
                map.put("allowTexting", castToBoolean(next[6]));
                map.put("selectable", castToBoolean(next[7]));
                map.put("userType", next[8]);

                users.add(map);
                next = reader.readNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    private interface UserDAO {
        @SqlBatch("INSERT INTO users (first_name, last_name, username, send_login_notifications, notify_of_logins, seconds_in_day, allow_texting, selectable, user_type, crypted_password, password_salt, persistence_token, single_access_token, perishable_token) VALUES (:firstName, :lastName, :username, :sendLoginNotifications, :notifyOfLogins, :secondsInDay, :allowTexting, :selectable, :userType, '400$8$3e$00af41f95b8ba344$68681f3a80dc86061665b0558bb197bdd9d2b97379052815036081cf5a482a9a', '1a576b4c2c9cdada50daffbebcfc39830fb7c796e29af75c2c5c58f0ce3f28e8763c4bf1f2130b192fb6e5737df3a99bd1ae270bf4b48c3efe55e6eb8370eb84', '3b974c03acb2a86bb2c393cf027896d20ba913e205b2cca38b4b2bc3c95fcdb08496936f98b1cf5a5742ae7ddf67cebea722f29fd2b04bdef22a5d24a1e49723', 'cCGRKm4YOX9XxvckAbwi', 'DiOuket1AJEeoNdM-diY')")
        void create(@BindMap List<Map<String, ?>> users);
    }
}
