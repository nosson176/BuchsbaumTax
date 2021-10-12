package com.buchsbaumtax.migration;

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
            map.put("userType", row[8]);
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

            smartViewDAO.create(map);
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
            TaxPersonal primary = taxPersonals.stream()
                    .filter(tp -> Objects.nonNull(tp.getCategory()))
                    .filter(tp -> tp.getCategory().equals("PRI."))
                    .findFirst()
                    .orElse(null);
            TaxPersonal secondary = taxPersonals.stream()
                    .filter(tp -> Objects.nonNull(tp.getCategory()))
                    .filter(tp -> tp.getCategory().equals("SEC."))
                    .findFirst()
                    .orElse(null);

            String displayName = null;
            boolean primaryExists = primary != null && primary.getFirstName() != null;
            boolean secondaryExists = secondary != null && secondary.getFirstName() != null;
            if (primaryExists && secondaryExists) {
                displayName = primary.getFirstName() + " - " + secondary.getFirstName();
            }
            else if (primaryExists) {
                displayName = primary.getFirstName();
            }

            List<Contact> contacts = contactDAO.getForClient(client.getId());
            String mainDetail = contacts.stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(Contact::getMainDetail)
                    .orElse(null);

            client.setDisplayName(displayName);
            client.setDisplayPhone(mainDetail);
            clientDAO.update(client);
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
                    ex.printStackTrace();
                    return null;
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

        @SqlUpdate("UPDATE clients SET status = :status, owes_status = :owesStatus, periodical = :periodical, last_name = last_name, archived = :archived, display_name = :displayName, display_phone = :displayPhone WHERE id = :id")
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
        @SqlUpdate("INSERT INTO logs (client_id, years, alarm_user_id, alert, alarm_complete, alarm_date, alarm_time, log_date, priority, note, seconds_spent, archived) VALUES (:clientId, :years, :alarmUserId, :alert, :alarmComplete, :alarmDate, :alarmTime, :logDate, :priority, :note, :secondsSpent, :archived)")
        void create(@BindMap Map<String, ?> log);
    }

    private interface FilingDAO {
        @SqlUpdate("INSERT INTO filings (tax_form, status, status_detail, status_date, memo, include_in_refund, owes, paid, include_fee, owes_fee, paid_fee, file_type, refund, rebate, completed, delivery_contact, second_delivery_contact, date_filed, tax_year_id, filing_type) VALUES (:taxForm, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :includeFee, :owesFee, :paidFee, :fileType, :refund, :rebate, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :taxYearId, :filingType)")
        void create(@BindMap Map<String, ?> filing);

        @SqlUpdate("INSERT INTO filings (state, status, status_detail, status_date, memo, include_in_refund, owes, paid, refund, completed, delivery_contact, second_delivery_contact, date_filed, tax_year_id, filing_type, file_type) VALUES (:state, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :refund, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :taxYearId, :filingType, :fileType)")
        void createState(@BindMap Map<String, ?> stateFiling);

        @SqlUpdate("INSERT INTO filings (status, status_date, amount, completed, tax_form, date_filed, tax_year_id, filing_type) VALUES (:status, :statusDate, :amount, :completed, :taxForm, :dateFiled, :taxYearId, :filingType)")
        void createExt(@BindMap Map<String, ?> extFiling);
    }

    private interface FeeDAO {
        @SqlUpdate("INSERT INTO fees (client_id, year, status, status_detail, fee_type, manual_amount, paid_amount, include, rate, date_fee, sum, archived) VALUES (:clientId, :year, :status, :statusDetail, :feeType, :manualAmount, :paidAmount, :include, :rate, :dateFee, :sum, :archived)")
        void create(@BindMap Map<String, ?> fee);
    }

    private interface SmartViewDAO {
        @SqlUpdate("INSERT INTO smartviews (user_name, user_id, name, sort_number, archived) VALUES (:userName, :userId, :name, :sortNumber, :archived)")
        void create(@BindMap Map<String, ?> smartView);
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

        System.out.println("Uploading fees...");
        List<String[]> fees = migration.parseCSV(root + "fees.csv");
        migration.csvToFees(fees);
        System.out.println("Fees completed.");

        System.out.println("Uploading smartviews...");
        List<String[]> smartViews = migration.parseCSV(root + "smartviews.csv");
        migration.csvToSmartViews(smartViews);
        System.out.println("Smartviews completed.");
    }
}
