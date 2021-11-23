package com.buchsbaumtax.migration;

import com.buchsbaumtax.app.domain.DisplayFields;
import com.buchsbaumtax.core.model.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class Migration {
    private String rootPath;
    private Handle handle;
    private Map<String, Integer> clientIds = new HashMap<>();
    private Map<String, Integer> taxYearIds = new HashMap<>();
    private Map<String, Integer> taxGroupIds = new HashMap<>();
    private Map<String, Integer> smartViewIds = new HashMap<>();
    private Map<String, String> statusMap = new HashMap<>();
    private Map<String, String> owesStatusMap = new HashMap<>();
    private Map<String, String> currenciesMap = new HashMap<>();
    private Map<String, String> categoriesMap = new HashMap<>();
    private Map<String, String> taxGroupMap = new HashMap<>();
    private Map<String, String> taxTypeMap = new HashMap<>();

    private Migration(String rootPath) {
        this.rootPath = rootPath;
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
        List<String[]> statuses = parseCSV(rootPath + "client_statuses.csv");
        for (String[] row : statuses) {
            statusMap.put(row[0], row[1]);
        }

        List<String[]> owesStatuses = parseCSV(rootPath + "owes_statuses.csv");
        for (String[] row : owesStatuses) {
            owesStatusMap.put(row[0], row[1]);
        }

        List<String[]> currencies = parseCSV(rootPath + "currencies.csv");
        for (String[] row : currencies) {
            currenciesMap.put(row[0], row[1]);
        }

        List<String[]> categories = parseCSV(rootPath + "categories.csv");
        for (String[] row : categories) {
            categoriesMap.put(row[0], row[1]);
        }

        List<String[]> taxGroups = parseCSV(rootPath + "tax_groups.csv");
        for (String[] row : taxGroups) {
            taxGroupMap.put(row[0], row[1]);
        }

        List<String[]> taxTypes = parseCSV(rootPath + "tax_types.csv");
        for (String[] row : taxTypes) {
            taxTypeMap.put(row[0], row[2]);
        }
    }

    private List<String[]> parseCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try {
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().withFieldAsNull(CSVReaderNullFieldIndicator.BOTH).build();
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(rfc4180Parser);
            CSVReader reader = csvReaderBuilder.build();

            String[] next = reader.readNext();
            while (next != null) {
                data.add(next);
                next = reader.readNext();
            }

            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void csvToClient(List<String[]> clients) {
        ClientDAO clientDAO = handle.attach(ClientDAO.class);

        for (String[] row : clients) {
            Map<String, Object> map = new HashMap<>();

            map.put("status", statusMap.get(row[1]));
            map.put("owesStatus", owesStatusMap.get(row[2]));
            map.put("periodical", row[3]);
            map.put("lastName", row[4]);
            map.put("archived", castToBoolean(row[5]));

            int clientId = clientDAO.create(map);
            clientIds.put(row[0], clientId);
        }
    }

    private void csvToContact(List<String[]> contacts) {
        ContactDAO contactDAO = handle.attach(ContactDAO.class);

        for (String[] row : contacts) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[1]));
            map.put("contactType", row[2]);
            map.put("memo", row[3]);
            map.put("mainDetail", row[4]);
            map.put("secondaryDetail", row[5]);
            map.put("state", row[6]);
            map.put("zip", row[7]);
            map.put("enabled", castToBoolean(row[8]));

            contactDAO.create(map);
        }
    }

    private void csvToExchangeRate(List<String[]> exchangeRates) {
        ExchangeRateDAO exchangeRateDAO = handle.attach(ExchangeRateDAO.class);

        for (String[] row : exchangeRates) {
            Map<String, Object> map = new HashMap<>();

            map.put("currency", row[0]);
            map.put("year", row[1]);
            map.put("show", castToBoolean(row[2]));
            map.put("rate", castToDouble(row[3]));

            exchangeRateDAO.create(map);
        }
    }

    private void csvToFbarBreakdown(List<String[]> fbarBreakdowns) {
        FbarBreakdownDAO fbarBreakdownDAO = handle.attach(FbarBreakdownDAO.class);

        for (String[] row : fbarBreakdowns) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[0]));
            map.put("years", row[1]);
            map.put("category", categoriesMap.get(row[2]));
            map.put("taxGroup", taxGroupMap.get(row[3]));
            map.put("taxType", taxTypeMap.get(row[4]));
            map.put("part", row[5]);
            map.put("currency", row[6]);
            map.put("frequency", castToInt(row[7]));
            map.put("documents", row[8]);
            map.put("description", row[9]);
            map.put("amount", castToDouble(row[10]));
            map.put("depend", row[11]);
            map.put("include", castToBoolean(row[12]));
            map.put("archived", castToBoolean(row[13]));

            fbarBreakdownDAO.create(map);
        }
    }

    private void csvToIncomeBreakdown(List<String[]> incomeBreakdowns) {
        IncomeBreakdownDAO incomeBreakdownDAO = handle.attach(IncomeBreakdownDAO.class);

        for (String[] row : incomeBreakdowns) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[0]));
            map.put("years", row[1]);
            map.put("category", categoriesMap.get(row[2]));
            map.put("taxGroup", taxGroupMap.get(row[3]));
            map.put("taxType", taxTypeMap.get(row[4]));
            map.put("job", row[5]);
            map.put("currency", row[6]);
            map.put("frequency", castToInt(row[7]));
            map.put("documents", row[8]);
            map.put("description", row[9]);
            map.put("amount", castToDouble(row[10]));
            map.put("exclusion", castToBoolean(row[11]));
            map.put("include", castToBoolean(row[12]));
            map.put("archived", castToBoolean(row[13]));
            map.put("depend", row[14]);

            incomeBreakdownDAO.create(map);
        }
    }

    private void csvToYearDetail(List<String[]> yearDetails) {
        YearDetailDAO yearDetailDAO = handle.attach(YearDetailDAO.class);

        for (String[] row : yearDetails) {
            Map<String, Object> map = new HashMap<>();

            map.put("year", row[0]);
            map.put("deductionMarriedFilingJointly", castToInt(row[1]));
            map.put("deductionHeadOfHousehold", castToInt(row[2]));
            map.put("deductionSingleAndMarriedFilingSeparately", castToInt(row[3]));
            map.put("deductionMarriedFilingJointlyAmount", castToInt(row[4]));
            map.put("deductionMarriedFilingSeparatelyAmount", castToInt(row[5]));
            map.put("deductionSingleAndHeadOfHouseholdAmount", castToInt(row[6]));
            map.put("ceilingSingleAndHeadOfHousehold", castToInt(row[7]));
            map.put("exemption", castToInt(row[8]));
            map.put("credit8812AnnualDeduction", castToInt(row[9]));
            map.put("ceilingSelfEmployment", castToInt(row[10]));
            map.put("exclusion2555", castToInt(row[11]));
            map.put("foreignAnnual", row[12]);
            map.put("foreignMonthly", row[13]);
            map.put("foreignSecondaryAnnual", row[14]);
            map.put("foreignSecondaryMonthly", row[15]);
            map.put("dollarAnnual", row[16]);
            map.put("dollarMonthly", row[17]);
            map.put("dollarSecondaryAnnual", row[18]);
            map.put("dollarSecondaryMonthly", row[19]);
            map.put("additional8812ChildCredit", row[20]);
            map.put("ceilingMarriedFilingJointly", castToInt(row[21]));
            map.put("ceilingMarriedFilingSeparately", castToInt(row[22]));

            yearDetailDAO.create(map);
        }
    }

    private void csvToTaxYears(List<String[]> taxYears) {
        TaxYearDAO taxYearDAO = handle.attach(TaxYearDAO.class);

        for (String[] row : taxYears) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[1]));
            map.put("year", row[2]);
            map.put("archived", castToBoolean(row[3]));
            map.put("irsHistory", castToBoolean(row[4]));

            try {
                int taxYearId = taxYearDAO.create(map);
                taxYearIds.put(row[0], taxYearId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void csvToUser(List<String[]> users) {
        UserDAO userDAO = handle.attach(UserDAO.class);

        for (String[] row : users) {
            Map<String, Object> map = new HashMap<>();
            if (row[1] == null) {
                map.put("firstName", null);
                map.put("lastName", null);
            }
            else {
                String[] nameList = row[1].split("\\s+");

                map.put("firstName", nameList[0]);
                if (nameList.length > 1) {
                    map.put("lastName", nameList[1]);
                }
                else {
                    map.put("lastName", null);
                }
            }
            map.put("username", row[2]);
            map.put("sendLoginNotifications", castToBoolean(row[3]));
            map.put("notifyOfLogins", castToBoolean(row[4]));
            map.put("secondsInDay", castToInt(row[5]));
            map.put("allowTexting", castToBoolean(row[6]));
            map.put("selectable", castToBoolean(row[7]));
            if (row[8].equals("User")) {
                map.put("userType", "user");
            }
            else if (row[8].equals("Full Access")) {
                map.put("userType", "admin");
            }
            else {
                map.put("userType", row[8]);
            }
            map.put("password", "1000:a55d2a919684268d8d14138f177119ec50a707cd42436edc:7e7a159e2e57b31f2523e750cb9ac98d85e50d332dd1ac0a");

            userDAO.create(map);
        }
    }

    private void csvToLogs(List<String[]> logs) {
        LogDAO logDAO = handle.attach(LogDAO.class);

        for (String[] row : logs) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[0]));
            map.put("years", row[1]);
            map.put("alarmUserName", row[2]);
            UserDAO userDAO = handle.attach(UserDAO.class);
            User user = userDAO.getByUsername(row[2]);
            if (user != null) {
                map.put("alarmUserId", user.getId());
            }
            else {
                map.put("alarmUserId", null);
            }
            map.put("alert", castToBoolean(row[3]));
            map.put("alarmComplete", castToBoolean(row[4]));
            map.put("alarmDate", parseDate(row[5]));
            map.put("alarmTime", row[6]);
            map.put("logDate", parseDate(row[7]));
            map.put("priority", castToInt(row[8]));
            map.put("note", row[9]);
            map.put("secondsSpent", getTotalSeconds(row[10]));
            map.put("archived", castToBoolean(row[11]));

            logDAO.create(map);
        }
    }

    private void csvToTaxPersonals(List<String[]> taxPersonals) {
        TaxPersonalDAO taxPersonalDAO = handle.attach(TaxPersonalDAO.class);

        for (String[] row : taxPersonals) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[0]));
            map.put("category", categoriesMap.get(row[1]));
            map.put("include", castToBoolean(row[2]));
            map.put("language", row[3]);
            map.put("relation", row[4]);
            map.put("firstName", row[5]);
            map.put("middleInitial", row[6]);
            map.put("lastName", row[7]);
            map.put("dateOfBirth", parseDate(row[8]));

            map.put("ssn", row[9]);
            map.put("informal", row[10]);

            taxPersonalDAO.create(map);
        }
    }

    private void csvTOFilings(List<String[]> filings) {
        FilingDAO filingDAO = handle.attach(FilingDAO.class);

        for (String[] row : filings) {

            ArrayList<String> federalFiling = new ArrayList<>(Arrays.asList(row).subList(6, 23));
            federalFiling.subList(7, 11).clear();
            federalFiling.remove(8);
            Map<String, Object> map = setFilingData(federalFiling);
            map.put("taxForm", row[5]);
            map.put("includeFee", castToBoolean(row[13]));
            map.put("owesFee", castToDouble(row[14]));
            map.put("paidFee", castToDouble(row[15]));
            map.put("fileType", row[16]);
            map.put("rebate", castToDouble(row[18]));
            map.put("taxYearId", taxYearIds.get(row[0]));
            map.put("filingType", "federal");

            filingDAO.create(map);

            if (row[23] != null) {
                ArrayList<String> stateFiling = new ArrayList<>(Arrays.asList(row).subList(24, 36));
                Map<String, Object> stateMap = setFilingData(stateFiling);
                stateMap.put("state", row[23]);
                stateMap.put("filingType", "state");
                stateMap.put("fileType", null);
                stateMap.put("taxYearId", taxYearIds.get(row[0]));

                filingDAO.createState(stateMap);
            }

            if (row[36] != null) {
                ArrayList<String> state2Filing = new ArrayList<>(Arrays.asList(row).subList(37, 49));
                Map<String, Object> state2Map = setFilingData(state2Filing);
                state2Map.put("state", row[36]);
                state2Map.put("filingType", "state");
                state2Map.put("fileType", null);
                state2Map.put("taxYearId", taxYearIds.get(row[0]));

                filingDAO.createState(state2Map);
            }

            if (row[49] != null) {
                ArrayList<String> fbarFiling = new ArrayList<>(Arrays.asList(row).subList(49, 56));
                fbarFiling.addAll(Arrays.asList(null, null, null, null));
                fbarFiling.add(row[57]);
                Map<String, Object> fbarMap = setFilingData(fbarFiling);
                fbarMap.put("state", null);
                fbarMap.put("fileType", row[56]);
                fbarMap.put("filingType", "fbar");
                fbarMap.put("taxYearId", taxYearIds.get(row[0]));

                filingDAO.createState(fbarMap);
            }

            if (row[58] != null) {
                Map<String, Object> extMap = new HashMap<>();
                extMap.put("status", row[58]);
                extMap.put("statusDate", parseDate(row[59]));
                extMap.put("amount", castToInt(row[60]));
                extMap.put("completed", castToBoolean(row[61]));
                extMap.put("taxForm", row[62]);
                extMap.put("dateFiled", parseDate(row[63]));
                extMap.put("filingType", "ext");
                extMap.put("taxYearId", taxYearIds.get(row[0]));

                filingDAO.createExt(extMap);
            }
        }
    }

    private void csvToValueList(Map<String, List<String[]>> valueLists) {
        ValueListDAO valueListDAO = handle.attach(ValueListDAO.class);
        List<String> booleanFields = Arrays.asList("translationNeeded", "passive", "selfEmployment", "show", "include");
        List<String> integerFields = Arrays.asList("sortOrder", "parentId");
        Map<String, Integer> valueIds = new HashMap<>();

        for (Map.Entry<String, List<String[]>> entry : valueLists.entrySet()) {
            Map<String, Object> map = new LinkedHashMap<>();

            List<String[]> values = entry.getValue();
            String[] columns = values.get(0);

            map.put("key", entry.getKey());
            for (int i = 1; i < values.size(); i++) {
                map.put("sortOrder", null);
                map.put("value", null);
                map.put("parentId", null);
                map.put("translationNeeded", false);
                map.put("passive", false);
                map.put("selfEmployment", false);
                map.put("show", true);
                map.put("subType", null);
                map.put("include", true);

                String[] row = values.get(i);

                for (int j = 1; j < columns.length; j++) {
                    String column = columns[j];
                    String value = row[j];
                    if (integerFields.contains(column)) {
                        map.put(column, castToInt(value));
                    }
                    else if (booleanFields.contains(column)) {
                        map.put(column, castToBoolean(value));
                    }
                    else {
                        map.put(column, value);
                    }

                    if (column.equals("fmParentId")) {
                        if (map.get("key").equals("tax_type")) {
                            map.put("parentId", taxGroupIds.get(value));
                        }
                        else {
                            map.put("parentId", valueIds.get(value));
                        }
                    }
                }

                int id = valueListDAO.create(map);
                valueIds.put(row[0], id);
            }
        }
    }

    private void csvToTaxGroups(List<String[]> taxGroups) {
        TaxGroupDAO taxGroupDAO = handle.attach(TaxGroupDAO.class);

        for (String[] row : taxGroups) {
            Map<String, Object> map = new HashMap<>();

            map.put("value", row[1]);
            map.put("show", castToBoolean(row[2]));
            map.put("include", castToBoolean(row[3]));
            map.put("selfEmployment", castToBoolean(row[4]));
            map.put("passive", castToBoolean(row[5]));
            map.put("subType", row[6]);

            int id = taxGroupDAO.create(map);
            taxGroupIds.put(row[0], id);
        }
    }

    private void csvToFees(List<String[]> fees) {
        FeeDAO feeDAO = handle.attach(FeeDAO.class);

        for (String[] row : fees) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[0]));
            map.put("year", row[1]);
            map.put("status", row[2]);
            map.put("statusDetail", row[3]);
            map.put("feeType", row[4]);
            map.put("manualAmount", castToDouble(row[5]));
            map.put("paidAmount", castToDouble(row[6]));
            map.put("include", castToBoolean(row[7]));
            map.put("rate", castToDouble(row[8]));
            map.put("dateFee", parseDate(row[9]));
            map.put("sum", castToBoolean(row[10]));
            map.put("archived", castToBoolean(row[11]));

            feeDAO.create(map);
        }
    }

    private void csvToSmartViews(List<String[]> smartViews) {
        SmartViewDAO smartViewDAO = handle.attach(SmartViewDAO.class);
        UserDAO userDAO = handle.attach(UserDAO.class);

        for (String[] row : smartViews) {
            Map<String, Object> map = new HashMap<>();

            map.put("userName", row[1]);
            User user = userDAO.getByUsername(row[1].toUpperCase());
            if (user != null) {
                map.put("userId", user.getId());
            }
            else {
                map.put("userId", null);
            }
            map.put("name", row[2]);
            map.put("sortNumber", castToInt(row[3]));
            map.put("archived", castToBoolean(row[4]));

            int id = smartViewDAO.create(map);
            smartViewIds.put(row[0], id);
        }
    }

    private void csvToSmartViewLines(List<String[]> smartViews) {
        SmartViewLineDAO smartViewLineDAO = handle.attach(SmartViewLineDAO.class);

        for (String[] row : smartViews) {
            Map<String, Object> map = new HashMap<>();

            map.put("smartViewId", smartViewIds.get(row[0]));
            map.put("query", castToInt(row[1]));
            String[] classMethod = new String[]{null, null};
            if (row[2] != null && !row[2].equals("")) {
                classMethod = row[2].split("::");
            }
            if (classMethod.length == 2) {
                map.put("classToJoin", classMethod[0]);
                map.put("fieldToSearch", classMethod[1]);
            }
            else {
                map.put("classToJoin", classMethod[0]);
                map.put("fieldToSearch", null);
            }
            map.put("searchValue", row[3]);

            smartViewLineDAO.create(map);
        }

    }

    private void csvToChecklists(List<String[]> checklists) {
        ChecklistDAO checklistDAO = handle.attach(ChecklistDAO.class);

        for (String[] row : checklists) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[0]));
            map.put("taxYearId", taxYearIds.get(row[1]));
            map.put("memo", row[2]);
            map.put("sortNumber", castToInt(row[3]));
            map.put("finished", castToBoolean(row[4]));
            map.put("translated", castToBoolean(row[5]));

            checklistDAO.create(map);
        }
    }

    private Map<String, Object> setFilingData(List<String> row) {
        Map<String, Object> map = new HashMap<>();

        map.put("status", row.get(0));
        map.put("statusDetail", row.get(1));
        map.put("statusDate", parseDate(row.get(2)));
        map.put("memo", row.get(3));
        map.put("includeInRefund", castToBoolean(row.get(4)));
        map.put("owes", castToDouble(row.get(5)));
        map.put("paid", castToDouble(row.get(6)));
        map.put("refund", castToDouble(row.get(7)));
        map.put("completed", castToBoolean(row.get(8)));
        map.put("deliveryContact", row.get(9));
        map.put("secondDeliveryContact", row.get(10));
        map.put("dateFiled", parseDate(row.get(11)));

        return map;
    }

    private void setDisplayFields() {
        ClientDAO clientDAO = handle.attach(ClientDAO.class);
        TaxPersonalDAO taxPersonalDAO = handle.attach(TaxPersonalDAO.class);
        ContactDAO contactDAO = handle.attach(ContactDAO.class);
        List<Client> clients = clientDAO.getAll();

        for (Client client : clients) {
            List<TaxPersonal> taxPersonals = taxPersonalDAO.getForClient(client.getId());
            List<Contact> contacts = contactDAO.getForClient(client.getId());

            DisplayFields displayFields = new DisplayFields();
            String displayName = displayFields.getDisplayName(taxPersonals);
            String mainDetail = displayFields.getDisplayPhone(contacts);

            client.setDisplayName(displayName);
            client.setDisplayPhone(mainDetail);
            clientDAO.update(client);
        }
    }

    private void setClientCreated() {
        ClientDAO clientDAO = handle.attach(ClientDAO.class);
        LogDAO logDAO = handle.attach(LogDAO.class);
        List<Client> clients = clientDAO.getAll();

        for (Client client : clients) {
            Date firstLog = logDAO.getForClient(client.getId()).stream().map(Log::getLogDate).filter(Objects::nonNull).min(Date::compareTo).orElse(null);
            if (firstLog != null) {
                client.setCreated(firstLog);
                clientDAO.update(client);
            }
        }
    }

    private boolean castToBoolean(String str) {
        return str != null && (str.equals("1") || str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes"));
    }

    private Integer castToInt(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Integer.parseInt(str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Double castToDouble(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Double.parseDouble(str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date parseDate(String str) {
        if (str == null) {
            return null;
        }
        Date date;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy").parse(str);
        }
        catch (Exception e) {
            try {
                date = new SimpleDateFormat("MM.dd.yyyy").parse(str);
            }
            catch (Exception exc) {
                e.printStackTrace();
                date = null;
            }
        }
        return date;
    }

    private Long getTotalSeconds(String str) {
        if (str == null) {
            return null;
        }
        else {
            String[] parts = str.split(":");
            try {
                return Duration.parse(String.format("PT%sH%sM%sS", parts[0], parts[1], parts[2])).getSeconds();
            }
            catch (Exception e) {
                try {
                    return Duration.parse(String.format("PT%sM%sS", parts[0], parts[1])).getSeconds();
                }
                catch (Exception ex) {
                    try {
                        return Long.parseLong(str);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return null;
                    }
                }
            }
        }
    }

    private interface ClientDAO {
        @RegisterFieldMapper(Client.class)
        @SqlUpdate("INSERT INTO clients (status, owes_status, periodical, last_name, archived) VALUES (:status, :owesStatus, :periodical, :lastName, :archived)")
        @GetGeneratedKeys
        int create(@BindMap Map<String, ?> client);

        @RegisterFieldMapper(Client.class)
        @SqlQuery("SELECT * FROM clients ORDER BY id")
        List<Client> getAll();

        @RegisterFieldMapper(Client.class)
        @SqlQuery("SELECT * FROM clients WHERE id = :id")
        Client get(@Bind("id") int id);

        @SqlUpdate("UPDATE clients SET status = :status, owes_status = :owesStatus, periodical = :periodical, last_name = last_name, archived = :archived, display_name = :displayName, display_phone = :displayPhone, created = :created WHERE id = :id")
        void update(@BindBean Client client);
    }

    private interface ContactDAO {
        @SqlUpdate("INSERT INTO contacts (client_id, contact_type, memo, main_detail, secondary_detail, state, zip, enabled) VALUES (:clientId, :contactType, :memo, :mainDetail, :secondaryDetail, :state, :zip, :enabled)")
        void create(@BindMap Map<String, ?> contact);

        @RegisterFieldMapper(Contact.class)
        @SqlQuery("SELECT * FROM contacts WHERE client_id = :clientId ORDER BY id")
        List<Contact> getForClient(@Bind("clientId") int clientId);
    }

    private interface ExchangeRateDAO {
        @SqlUpdate("INSERT INTO exchange_rates (rate, show, currency, year) VALUES (:rate, :show, :currency, :year)")
        void create(@BindMap Map<String, ?> exchangeRate);
    }

    private interface FbarBreakdownDAO {
        @SqlUpdate("INSERT INTO fbar_breakdowns (client_id, years, category, tax_group, tax_type, part, currency, frequency, documents, description, amount, depend, include, archived) VALUES (:clientId, :years, :category, :taxGroup, :taxType, :part, :currency, :frequency, :documents, :description, :amount, :depend, :include, :archived)")
        void create(@BindMap Map<String, ?> fbarBreakdown);
    }

    private interface IncomeBreakdownDAO {
        @SqlUpdate("INSERT INTO income_breakdowns (client_id, years, category, tax_group, tax_type, job, currency, frequency, documents, description, amount, exclusion, include, archived, depend) VALUES (:clientId, :years, :category, :taxGroup, :taxType, :job, :currency, :frequency, :documents, :description, :amount, :exclusion, :include, :archived, :depend)")
        void create(@BindMap Map<String, ?> incomeBreakdown);
    }

    private interface YearDetailDAO {
        @SqlUpdate("INSERT INTO year_details (year, deduction_married_filing_jointly, deduction_head_of_household, deduction_single_and_married_filing_separately, deduction_married_filing_jointly_amount, deduction_married_filing_separately_amount, deduction_single_and_head_of_household_amount, ceiling_single_and_head_of_household, exemption, credit_8812_annual_deduction, ceiling_self_employment, exclusion_2555, foreign_annual, foreign_monthly, foreign_secondary_annual, foreign_secondary_monthly, dollar_annual, dollar_monthly, dollar_secondary_annual, dollar_secondary_monthly, additional_8812_child_credit, ceiling_married_filing_jointly, ceiling_married_filing_separately) VALUES (:year, :deductionMarriedFilingJointly, :deductionHeadOfHousehold, :deductionSingleAndMarriedFilingSeparately, :deductionMarriedFilingJointlyAmount,:deductionMarriedFilingSeparatelyAmount, :deductionSingleAndHeadOfHouseholdAmount, :ceilingSingleAndHeadOfHousehold, :exemption, :credit8812AnnualDeduction, :ceilingSelfEmployment, :exclusion2555, :foreignAnnual, :foreignMonthly, :foreignSecondaryAnnual, :foreignSecondaryMonthly, :dollarAnnual, :dollarMonthly, :dollarSecondaryAnnual, :dollarSecondaryMonthly, :additional8812ChildCredit, :ceilingMarriedFilingJointly, :ceilingMarriedFilingSeparately)")
        void create(@BindMap Map<String, ?> yearDetail);
    }

    private interface TaxYearDAO {
        @RegisterFieldMapper(TaxYear.class)
        @SqlUpdate("INSERT INTO tax_years (client_id, year, archived, irs_history) VALUES (:clientId, :year, :archived, :irsHistory)")
        @GetGeneratedKeys
        int create(@BindMap Map<String, ?> taxYear);
    }

    private interface UserDAO {
        @SqlUpdate("INSERT INTO users (first_name, last_name, username, send_login_notifications, notify_of_logins, seconds_in_day, allow_texting, selectable, user_type, password) VALUES (:firstName, :lastName, :username, :sendLoginNotifications, :notifyOfLogins, :secondsInDay, :allowTexting, :selectable, :userType, :password)")
        void create(@BindMap Map<String, ?> user);

        @RegisterFieldMapper(User.class)
        @SqlQuery("SELECT * FROM users WHERE username = :username")
        User getByUsername(@Bind("username") String username);
    }

    private interface TaxPersonalDAO {
        @SqlUpdate("INSERT INTO tax_personals (client_id, category, include, language, relation, first_name, middle_initial, last_name, date_of_birth, ssn, informal) VALUES (:clientId, :category, :include, :language, :relation, :firstName, :middleInitial, :lastName, :dateOfBirth, :ssn, :informal)")
        void create(@BindMap Map<String, ?> taxPersonal);

        @RegisterFieldMapper(TaxPersonal.class)
        @SqlQuery("SELECT * FROM tax_personals WHERE client_id = :clientId")
        List<TaxPersonal> getForClient(@Bind("clientId") int clientId);
    }

    private interface LogDAO {
        @SqlUpdate("INSERT INTO logs (client_id, years, alarm_user_name, alarm_user_id, alert, alarm_complete, alarm_date, alarm_time, log_date, priority, note, seconds_spent, archived) VALUES (:clientId, :years, :alarmUserName, :alarmUserId, :alert, :alarmComplete, :alarmDate, :alarmTime, :logDate, :priority, :note, :secondsSpent, :archived)")
        void create(@BindMap Map<String, ?> log);

        @RegisterFieldMapper(Log.class)
        @SqlQuery("SELECT * FROM logs WHERE client_id = :clientId")
        List<Log> getForClient(@Bind("clientId") int clientId);
    }

    private interface FilingDAO {
        @SqlUpdate("INSERT INTO filings (tax_form, status, status_detail, status_date, memo, include_in_refund, owes, paid, include_fee, owes_fee, paid_fee, file_type, refund, rebate, completed, delivery_contact, second_delivery_contact, date_filed, tax_year_id, filing_type) VALUES (:taxForm, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :includeFee, :owesFee, :paidFee, :fileType, :refund, :rebate, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :taxYearId, :filingType)")
        void create(@BindMap Map<String, ?> filing);

        @SqlUpdate("INSERT INTO filings (state, status, status_detail, status_date, memo, include_in_refund, owes, paid, refund, completed, delivery_contact, second_delivery_contact, date_filed, tax_year_id, filing_type, file_type) VALUES (:state, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :refund, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :taxYearId, :filingType, :fileType)")
        void createState(@BindMap Map<String, ?> stateFiling);

        @SqlUpdate("INSERT INTO filings (status, status_date, amount, completed, tax_form, date_filed, tax_year_id, filing_type) VALUES (:status, :statusDate, :amount, :completed, :taxForm, :dateFiled, :taxYearId, :filingType)")
        void createExt(@BindMap Map<String, ?> extFiling);
    }

    private interface ValueListDAO {
        @RegisterFieldMapper(Value.class)
        @SqlUpdate("INSERT INTO value_lists (sort_order, key, value, parent_id, translation_needed, show, include) VALUES (:sortOrder, :key, :value, :parentId, :translationNeeded, :show, :include)")
        @GetGeneratedKeys
        int create(@BindMap Map<String, ?> value_list);
    }

    private interface TaxGroupDAO {
        @RegisterFieldMapper(TaxGroup.class)
        @SqlUpdate("INSERT INTO tax_groups (value, show, include, self_employment, passive, sub_type) VALUES (:value, :show, :include, :selfEmployment, :passive, :subType)")
        @GetGeneratedKeys
        int create(@BindMap Map<String, ?> tax_group);
    }

    private interface FeeDAO {
        @SqlUpdate("INSERT INTO fees (client_id, year, status, status_detail, fee_type, manual_amount, paid_amount, include, rate, date_fee, sum, archived) VALUES (:clientId, :year, :status, :statusDetail, :feeType, :manualAmount, :paidAmount, :include, :rate, :dateFee, :sum, :archived)")
        void create(@BindMap Map<String, ?> fee);
    }

    private interface SmartViewDAO {
        @SqlUpdate("INSERT INTO smartviews (user_name, user_id, name, sort_number, archived) VALUES (:userName, :userId, :name, :sortNumber, :archived)")
        @GetGeneratedKeys
        int create(@BindMap Map<String, ?> smartView);
    }

    private interface SmartViewLineDAO {
        @SqlUpdate("INSERT INTO smartview_lines (smartview_id, query, class_to_join, field_to_search, search_value) VALUES (:smartViewId, :query, :classToJoin, :fieldToSearch, :searchValue)")
        void create(@BindMap Map<String, ?> smartViewLine);
    }

    private interface ChecklistDAO {
        @SqlUpdate("INSERT INTO checklist_items (client_id, tax_year_id, memo, sort_number, finished, translated) VALUES (:clientId, :taxYearId, :memo, :sortNumber, :finished, :translated)")
        void create(@BindMap Map<String, ?> checklist);
    }

    public static void main(String[] args) {
        String root = "C:\\Users\\shalo\\Downloads\\buchsbaum-main\\buchsbaum-main\\lib\\fm_uploads\\";
        Migration migration = new Migration(root);


        System.out.println("Uploading clients...");
        List<String[]> clients = migration.parseCSV(root + "clients.csv");
        migration.csvToClient(clients);
        System.out.println("Clients uploaded");

        System.out.println("Uploading contacts...");
        List<String[]> contacts = migration.parseCSV(root + "contacts.csv");
        migration.csvToContact(contacts);
        System.out.println("Contacts completed.");

        System.out.println("Uploading exchange rates...");
        List<String[]> exchangeRates = migration.parseCSV(root + "exchange_rates.csv");
        migration.csvToExchangeRate(exchangeRates);
        System.out.println("Exchange rates completed.");

        System.out.println("Uploading fbar breakdowns...");
        List<String[]> fbarBreakdowns = migration.parseCSV(root + "fbar_breakdowns.csv");
        migration.csvToFbarBreakdown(fbarBreakdowns);
        System.out.println("Fbar breakdowns completed.");

        System.out.println("Uploading income breakdowns...");
        List<String[]> incomeBreakdowns = migration.parseCSV(root + "inc_breakdowns.csv");
        migration.csvToIncomeBreakdown(incomeBreakdowns);
        System.out.println("Income breakdowns completed.");

        System.out.println("Uploading year details...");
        List<String[]> yearDetails = migration.parseCSV(root + "year_details.csv");
        migration.csvToYearDetail(yearDetails);
        System.out.println("Year details completed.");

        System.out.println("Uploading tax years...");
        List<String[]> taxYears = migration.parseCSV(root + "tax_years.csv");
        migration.csvToTaxYears(taxYears);
        System.out.println("Tax years completed.");

        System.out.println("Uploading users...");
        List<String[]> users = migration.parseCSV(root + "users.csv");
        migration.csvToUser(users);
        System.out.println("Users completed.");

        System.out.println("Uploading logs...");
        List<String[]> logs = migration.parseCSV(root + "logs.csv");
        migration.csvToLogs(logs);
        System.out.println("Logs completed.");

        System.out.println("Uploading tax personals...");
        List<String[]> taxPersonals = migration.parseCSV(root + "tax_personals.csv");
        migration.csvToTaxPersonals(taxPersonals);
        System.out.println("Tax personals completed.");

        System.out.println("Uploading filings...");
        List<String[]> filings = migration.parseCSV(root + "tax_years.csv");
        migration.csvTOFilings(filings);
        System.out.println("Filings completed.");

        migration.setDisplayFields();

        System.out.println("Uploading tax groups...");
        List<String[]> taxGroups = migration.parseCSV(root + "tax_groups.csv");
        migration.csvToTaxGroups(taxGroups);
        System.out.println("Tax groups completed.");

        System.out.println("Uploading value lists...");
        Map<String, List<String[]>> valueLists = new LinkedHashMap<>();

        List<String[]> checklistMemos = new ArrayList<>();
        String[] columns = new String[]{"fmId", "value", "show", "sortOrder", "translationNeeded"};
        checklistMemos.add(columns);
        List<String[]> memosData = migration.parseCSV(root + "cl_memos.csv");
        checklistMemos.addAll(memosData);
        valueLists.put("checklist_memo", checklistMemos);

        List<String[]> states = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        states.add(columns);
        List<String[]> statesData = migration.parseCSV(root + "states.csv");
        states.addAll(statesData);
        valueLists.put("state", states);

        List<String[]> currencies = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show"};
        currencies.add(columns);
        List<String[]> currencyData = migration.parseCSV(root + "currencies.csv");
        currencies.addAll(currencyData);
        valueLists.put("currency", currencies);

        List<String[]> feeTypes = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        feeTypes.add(columns);
        List<String[]> feeTypesData = migration.parseCSV(root + "fee_types.csv");
        feeTypes.addAll(feeTypesData);
        valueLists.put("fee_type", feeTypes);

        List<String[]> jobs = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        jobs.add(columns);
        List<String[]> jobsData = migration.parseCSV(root + "job.csv");
        jobs.addAll(jobsData);
        valueLists.put("job", jobs);

        List<String[]> categories = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        categories.add(columns);
        List<String[]> categoryData = migration.parseCSV(root + "categories.csv");
        categories.addAll(categoryData);
        valueLists.put("category", categories);

        List<String[]> parts = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        parts.add(columns);
        List<String[]> partData = migration.parseCSV(root + "fbar_part.csv");
        parts.addAll(partData);
        valueLists.put("part", parts);

        List<String[]> taxForms = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        taxForms.add(columns);
        List<String[]> taxFormData = migration.parseCSV(root + "tax_forms.csv");
        taxForms.addAll(taxFormData);
        valueLists.put("tax_form", taxForms);

        List<String[]> oweStatuses = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        oweStatuses.add(columns);
        List<String[]> owesStatusData = migration.parseCSV(root + "owes_statuses.csv");
        oweStatuses.addAll(owesStatusData);
        valueLists.put("owes_status", oweStatuses);

        List<String[]> contactTypes = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        contactTypes.add(columns);
        List<String[]> contactTypeData = migration.parseCSV(root + "contact_types.csv");
        contactTypes.addAll(contactTypeData);
        valueLists.put("contact_type", contactTypes);

        List<String[]> languages = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        languages.add(columns);
        List<String[]> languageData = migration.parseCSV(root + "languages.csv");
        languages.addAll(languageData);
        valueLists.put("language", languages);

        List<String[]> yearNames = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        yearNames.add(columns);
        List<String[]> yearNameData = migration.parseCSV(root + "year_names.csv");
        yearNames.addAll(yearNameData);
        valueLists.put("year_name", yearNames);

        List<String[]> relations = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        relations.add(columns);
        List<String[]> relationData = migration.parseCSV(root + "relations.csv");
        relations.addAll(relationData);
        valueLists.put("relation", relations);

        List<String[]> periodicals = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        periodicals.add(columns);
        List<String[]> periodicalData = migration.parseCSV(root + "periodicals.csv");
        periodicals.addAll(periodicalData);
        valueLists.put("periodical", periodicals);

        List<String[]> statuses = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show"};
        statuses.add(columns);
        List<String[]> statusData = migration.parseCSV(root + "client_statuses.csv");
        statuses.addAll(statusData);
        valueLists.put("status", statuses);

        List<String[]> fileTypes = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        fileTypes.add(columns);
        List<String[]> fileTypeData = migration.parseCSV(root + "file_types.csv");
        fileTypes.addAll(fileTypeData);
        valueLists.put("file_type", fileTypes);

        List<String[]> taxTypes = new ArrayList<>();
        columns = new String[]{"fmId", "fmParentId", "value", "show", "include"};
        taxTypes.add(columns);
        List<String[]> taxTypesData = migration.parseCSV(root + "tax_types.csv");
        taxTypes.addAll(taxTypesData);
        valueLists.put("tax_type", taxTypes);

        List<String[]> taxYearStatuses = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show"};
        taxYearStatuses.add(columns);
        List<String[]> taxYearStatusData = migration.parseCSV(root + "tax_year_statuses.csv");
        taxYearStatuses.addAll(taxYearStatusData);
        valueLists.put("tax_year_status", taxYearStatuses);

        List<String[]> taxYearStatusDetails = new ArrayList<>();
        columns = new String[]{"fmId", "fmParentId", "value", "show", "include"};
        taxYearStatusDetails.add(columns);
        List<String[]> taxYearStatusDetailsData = migration.parseCSV(root + "tax_year_status_details.csv");
        taxYearStatusDetails.addAll(taxYearStatusDetailsData);
        valueLists.put("tax_year_status_detail", taxYearStatusDetails);

        List<String[]> feeStatuses = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show"};
        feeStatuses.add(columns);
        List<String[]> feeStatusData = migration.parseCSV(root + "fee_statuses.csv");
        feeStatuses.addAll(feeStatusData);
        valueLists.put("fee_status", feeStatuses);

        List<String[]> feeStatusDetails = new ArrayList<>();
        columns = new String[]{"fmId", "fmParentId", "value", "show"};
        feeStatusDetails.add(columns);
        List<String[]> feeStatusDetailData = migration.parseCSV(root + "fee_status_details.csv");
        feeStatusDetails.addAll(feeStatusDetailData);
        valueLists.put("fee_status_detail", feeStatusDetails);

        List<String[]> stateStatuses = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        stateStatuses.add(columns);
        List<String[]> stateStatusData = migration.parseCSV(root + "state_statuses.csv");
        stateStatuses.addAll(stateStatusData);
        valueLists.put("state_status", stateStatuses);

        List<String[]> stateStatusDetails = new ArrayList<>();
        columns = new String[]{"fmId", "fmParentId", "value", "show", "sortOrder"};
        stateStatusDetails.add(columns);
        List<String[]> stateStatusDetailData = migration.parseCSV(root + "state_status_details.csv");
        stateStatusDetails.addAll(stateStatusDetailData);
        valueLists.put("state_status_detail", stateStatusDetails);

        List<String[]> fbarStatuses = new ArrayList<>();
        columns = new String[]{"fmId", "value", "show", "sortOrder"};
        fbarStatuses.add(columns);
        List<String[]> fbarStatusData = migration.parseCSV(root + "fbar_statuses.csv");
        fbarStatuses.addAll(fbarStatusData);
        valueLists.put("fbar_status", fbarStatuses);

        List<String[]> fbarStatusDetails = new ArrayList<>();
        columns = new String[]{"fmId", "fmParentId", "value", "show", "sortOrder"};
        fbarStatusDetails.add(columns);
        List<String[]> fbarStatusDetailData = migration.parseCSV(root + "fbar_status_details.csv");
        fbarStatusDetails.addAll(fbarStatusDetailData);
        valueLists.put("fbar_status_detail", fbarStatusDetails);

        migration.csvToValueList(valueLists);
        System.out.println("Value list completed.");

        System.out.println("Uploading fees...");
        List<String[]> fees = migration.parseCSV(root + "fees.csv");
        migration.csvToFees(fees);
        System.out.println("Fees completed.");

        System.out.println("Uploading smartviews...");
        List<String[]> smartViews = migration.parseCSV(root + "smartviews.csv");
        migration.csvToSmartViews(smartViews);
        System.out.println("Smartviews completed.");

        System.out.println("Uploading smartview lines...");
        List<String[]> smartViewLines = migration.parseCSV(root + "smartview_lines.csv");
        migration.csvToSmartViewLines(smartViewLines);
        System.out.println("Smartview lines completed.");

        System.out.println("Uploading checklists...");
        List<String[]> checklists = migration.parseCSV(root + "check_lists.csv");
        migration.csvToChecklists(checklists);
        System.out.println("Checklists completed.");

        migration.setClientCreated();
    }
}