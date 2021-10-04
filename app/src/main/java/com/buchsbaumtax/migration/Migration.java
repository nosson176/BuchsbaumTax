package com.buchsbaumtax.migration;

import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.User;
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
            map.put("zip", row[7]);
            map.put("enabled", castToBoolean(row[8]));

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

    private void csvToFbarBreakdown(List<String[]> fbarBreakdowns) {
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

            FbarBreakdownDAO fbarBreakdownDAO = handle.attach(FbarBreakdownDAO.class);
            fbarBreakdownDAO.create(map);
        }
    }

    private void csvToIncomeBreakdown(List<String[]> incomeBreakdowns) {
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

            IncomeBreakdownDAO incomeBreakdownDAO = handle.attach(IncomeBreakdownDAO.class);
            incomeBreakdownDAO.create(map);
        }
    }

    private void csvToYearDetail(List<String[]> yearDetails) {
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

            YearDetailDAO yearDetailDAO = handle.attach(YearDetailDAO.class);
            yearDetailDAO.create(map);
        }
    }

    private void csvToTaxYears(List<String[]> taxYears) {
        for (String[] row : taxYears) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[1]));
            map.put("year", row[2]);
            map.put("archived", castToBoolean(row[3]));
            map.put("irsHistory", castToBoolean(row[4]));

            TaxYearDAO taxYearDAO = handle.attach(TaxYearDAO.class);
            try {
                taxYearDAO.create(map);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void csvToUser(List<String[]> users) {
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

            UserDAO userDAO = handle.attach(UserDAO.class);
            userDAO.create(map);
        }
    }

    private void csvToLogs(List<String[]> logs) {
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

            LogDAO logDAO = handle.attach(LogDAO.class);
            logDAO.create(map);
        }
    }

    private void csvToTaxPersonals(List<String[]> taxPersonals) {
        for (String[] row : taxPersonals) {
            Map<String, Object> map = new HashMap<>();

            map.put("clientId", clientIds.get(row[0]));
            map.put("category", row[1]);
            map.put("include", castToBoolean(row[2]));
            map.put("language", row[3]);
            map.put("relation", row[4]);
            map.put("firstName", row[5]);
            map.put("middleInitial", row[6]);
            map.put("lastName", row[7]);
            map.put("dateOfBirth", parseDate(row[8]));

            map.put("ssn", row[9]);
            map.put("informal", row[10]);

            TaxPersonalDAO taxPersonalDAO = handle.attach(TaxPersonalDAO.class);
            taxPersonalDAO.create(map);
        }
    }

    private void csvTOFilings(List<String[]> filings) {
        for (String[] row : filings) {
            Map<String, Object> map = new HashMap<>();

            map.put("taxForm", row[5]);
            map.put("status", row[6]);
            map.put("statusDetail", row[7]);
            map.put("statusDate", parseDate(row[8]));
            map.put("memo", row[9]);
            map.put("includeInRefund", castToBoolean(row[10]));
            map.put("owes", castToDouble(row[11]));
            map.put("paid", castToDouble(row[12]));
            map.put("includeFee", castToBoolean(row[13]));
            map.put("owesFee", castToDouble(row[14]));
            map.put("paidFee", castToDouble(row[15]));
            map.put("fileType", row[16]);
            map.put("refund", castToDouble(row[17]));
            map.put("rebate", castToDouble(row[18]));
            map.put("completed", castToBoolean(row[19]));
            map.put("deliveryContact", row[20]);
            map.put("secondDeliveryContact", row[21]);
            map.put("dateFiled", parseDate(row[22]));
            map.put("currency", row[23]);

            FilingDAO filingDAO = handle.attach(FilingDAO.class);
            filingDAO.create(map);
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
        @SqlUpdate("INSERT INTO clients1 (status, owes_status, periodical, last_name, archived) VALUES (:status, :owesStatus, :periodical, :lastName, :archived)")
        @GetGeneratedKeys
        int create(@BindMap Map<String, ?> client);
    }

    private interface ContactDAO {
        @SqlUpdate("INSERT INTO contacts1 (client_id, contact_type, memo, main_detail, secondary_detail, state, zip, enabled) VALUES (:clientId, :contactType, :memo, :mainDetail, :secondaryDetail, :state, :zip, :enabled)")
        void create(@BindMap Map<String, ?> contact);
    }

    private interface ExchangeRateDAO {
        @SqlUpdate("INSERT INTO exchange_rates1 (rate, show, currency, year) VALUES (:rate, :show, :currency, :year)")
        void create(@BindMap Map<String, ?> exchangeRate);
    }

    private interface FbarBreakdownDAO {
        @SqlUpdate("INSERT INTO fbar_breakdowns1 (client_id, years, category, tax_group, tax_type, part, currency, frequency, documents, description, amount, depend, include, archived) VALUES (:clientId, :years, :category, :taxGroup, :taxType, :part, :currency, :frequency, :documents, :description, :amount, :depend, :include, :archived)")
        void create(@BindMap Map<String, ?> fbarBreakdown);
    }

    private interface IncomeBreakdownDAO {
        @SqlUpdate("INSERT INTO income_breakdowns1 (client_id, years, category, tax_group, tax_type, job, currency, frequency, documents, description, amount, exclusion, include, archived, depend) VALUES (:clientId, :years, :category, :taxGroup, :taxType, :job, :currency, :frequency, :documents, :description, :amount, :exclusion, :include, :archived, :depend)")
        void create(@BindMap Map<String, ?> incomeBreakdown);
    }

    private interface YearDetailDAO {
        @SqlUpdate("INSERT INTO year_details1 (year, deduction_married_filing_jointly, deduction_head_of_household, deduction_single_and_married_filing_separately, deduction_married_filing_jointly_amount, deduction_married_filing_separately_amount, deduction_single_and_head_of_household_amount, ceiling_single_and_head_of_household, exemption, credit_8812_annual_deduction, ceiling_self_employment, exclusion_2555, foreign_annual, foreign_monthly, foreign_secondary_annual, foreign_secondary_monthly, dollar_annual, dollar_monthly, dollar_secondary_annual, dollar_secondary_monthly, additional_8812_child_credit, ceiling_married_filing_jointly, ceiling_married_filing_separately) VALUES (:year, :deductionMarriedFilingJointly, :deductionHeadOfHousehold, :deductionSingleAndMarriedFilingSeparately, :deductionMarriedFilingJointlyAmount,:deductionMarriedFilingSeparatelyAmount, :deductionSingleAndHeadOfHouseholdAmount, :ceilingSingleAndHeadOfHousehold, :exemption, :credit8812AnnualDeduction, :ceilingSelfEmployment, :exclusion2555, :foreignAnnual, :foreignMonthly, :foreignSecondaryAnnual, :foreignSecondaryMonthly, :dollarAnnual, :dollarMonthly, :dollarSecondaryAnnual, :dollarSecondaryMonthly, :additional8812ChildCredit, :ceilingMarriedFilingJointly, :ceilingMarriedFilingSeparately)")
        void create(@BindMap Map<String, ?> yearDetail);
    }

    private interface TaxYearDAO {
        @SqlUpdate("INSERT INTO tax_years1 (client_id, year, archived, irs_history) VALUES (:clientId, :year, :archived, :irsHistory)")
        void create(@BindMap Map<String, ?> taxYear);
    }

    private interface UserDAO {
        @SqlUpdate("INSERT INTO users1 (first_name, last_name, username, send_login_notifications, notify_of_logins, seconds_in_day, allow_texting, selectable, user_type) VALUES (:firstName, :lastName, :username, :sendLoginNotifications, :notifyOfLogins, :secondsInDay, :allowTexting, :selectable, :userType)")
        void create(@BindMap Map<String, ?> user);

        @RegisterFieldMapper(User.class)
        @SqlQuery("SELECT * FROM users1 WHERE username = :username")
        User getByUsername(@Bind("username") String username);
    }

    private interface TaxPersonalDAO {
        @SqlUpdate("INSERT INTO tax_personals1 (client_id, category, include, language, relation, first_name, middle_initial, last_name, date_of_birth, ssn, informal) VALUES (:clientId, :category, :include, :language, :relation, :firstName, :middleInitial, :lastName, :dateOfBirth, :ssn, :informal)")
        void create(@BindMap Map<String, ?> taxPersonal);
    }

    private interface LogDAO {
        @SqlUpdate("INSERT INTO logs1 (client_id, years, alarm_user_id, alert, alarm_complete, alarm_date, alarm_time, log_date, priority, note, seconds_spent, archived) VALUES (:clientId, :years, :alarmUserId, :alert, :alarmComplete, :alarmDate, :alarmTime, :logDate, :priority, :note, :secondsSpent, :archived)")
        void create(@BindMap Map<String, ?> log);
    }

    private interface FilingDAO {
        @SqlUpdate("INSERT INTO filings1 (tax_form, status, status_detail, status_date, memo, include_in_refund, owes, paid, include_fee, owes_fee, paid_fee, file_type, refund, rebate, completed, delivery_contact, second_delivery_contact, date_filed, currency) VALUES (:taxForm, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :includeFee, :owesFee, :paidFee, :fileType, :refund, :rebate, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :currency)")
        void create(@BindMap Map<String, ?> filing);
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
    }
}
