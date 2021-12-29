package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.job.UpdateSmartviewsJob;
import com.buchsbaumtax.core.dao.*;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpdateSmartviews {
    private static final Logger Logger = LoggerFactory.getLogger(UpdateSmartviewsJob.class);
    List<Filing> filings;
    List<TaxYear> taxYears;
    List<Client> clients;
    List<Contact> contacts;
    List<IncomeBreakdown> incomeBreakdowns;
    List<Log> logs;
    List<TaxPersonal> taxPersonals;
    List<Fee> fees;
    List<ClientFlag> clientFlags;
    List<Smartview> smartviews;

    public void updateClients() {
        Logger.info("Starting UpdateSmartviews Job...");

        clients = Database.dao(ClientDAO.class).getAll();
        contacts = Database.dao(ContactDAO.class).getAll();
        incomeBreakdowns = Database.dao(IncomeBreakdownDAO.class).getAll();
        taxYears = Database.dao(TaxYearDAO.class).getAll();
        logs = Database.dao(LogDAO.class).getAll();
        taxPersonals = Database.dao(TaxPersonalDAO.class).getAll();
        filings = Database.dao(FilingDAO.class).getAll();
        fees = Database.dao(FeeDAO.class).getAll();
        clientFlags = Database.dao(ClientFlagDAO.class).getAll();
        smartviews = Database.dao(SmartviewDAO.class).getAll();

        Map<String, List<?>> dataMap = new HashMap<>();

        dataMap.put("Client", clients);
        dataMap.put("Contact", contacts);
        dataMap.put("IncomeBreakDown", incomeBreakdowns);
        dataMap.put("Filing", filings);
        dataMap.put("ClientFlag", clientFlags);
        dataMap.put("Log", logs);
        dataMap.put("TaxPersonal", taxPersonals);
        dataMap.put("Fee", fees);
        dataMap.put("TaxYear", taxYears);

        List<Smartview> updatedSmartviews = new ArrayList<>();

        for (Smartview smartview : smartviews) {
            List<SmartviewLine> smartviewLines = smartview.getSmartviewLines();
            Set<Integer> clientIds = new HashSet<>();
            List<?> filteredData = new ArrayList<>();

            for (SmartviewLine line : smartviewLines) {
                String classToJoin = line.getClassToJoin();
                String type = line.getType();
                Object searchValue = line.getSearchValue();
                String operator = line.getOperator();

                List<?> data = dataMap.get(classToJoin);
                if (data != null) {
                    Map<String, String> fieldTranslations = new HashMap<>();
                    fieldTranslations.put("comment", "memo");
                    fieldTranslations.put("yearName", "year");
                    fieldTranslations.put("type", "contactType");
                    fieldTranslations.put("taxYear", "year");
                    fieldTranslations.put("dateOfLog", "logDate");
                    fieldTranslations.put("pmtStatus", "status");
                    fieldTranslations.put("dateOfFirstLog", "created");

                    String fieldToSearch = line.getFieldToSearch();
                    if (fieldTranslations.containsKey(fieldToSearch)) {
                        fieldToSearch = fieldTranslations.get(fieldToSearch);
                    }


                    if (classToJoin.equals("TaxYear")) {
                        if (fieldToSearch != null && searchValue != null) {
                            filteredData = getTaxYearData(fieldToSearch, searchValue);
                        }
                    }
                    else if (classToJoin.equals("ClientFlag")) {
                        if (fieldToSearch.equals("userName")) {
                            User user = Database.dao(UserDAO.class).getByUsername((String)searchValue);
                            if (user != null) {
                                filteredData = clientFlags.stream().filter(f -> f.getClientId() == user.getId()).collect(Collectors.toList());
                            }
                        }
                        else if (fieldToSearch.equals("flagName")) {
                            Map<String, Integer> flagMappings = new HashMap<>();
                            flagMappings.put("BLACK", 1);
                            flagMappings.put("BLUE", 2);
                            flagMappings.put("GREEN", 3);
                            flagMappings.put("None", 4);
                            flagMappings.put("ORANGE", 5);
                            flagMappings.put("PINK", 6);
                            flagMappings.put("PURPLE", 7);
                            flagMappings.put("RED", 8);
                            flagMappings.put("TURQUOISE", 9);
                            flagMappings.put("YELLOW", 10);

                            if (searchValue != null) {
                                filteredData = clientFlags.stream().filter(f -> f.getFlag() == flagMappings.get(searchValue)).collect(Collectors.toList());
                            }
                        }
                    }
                    else if (fieldToSearch.equals("yearOfFirstLog")) {
                        Map<Integer, Log> yearOfFirstLog = logs.stream()
                                .filter(l -> l.getLogDate() != null)
                                .collect(Collectors.toMap(Log::getClientId, Function.identity(),
                                        BinaryOperator.minBy(Comparator.comparing(Log::getLogDate))));
                        filteredData = yearOfFirstLog.values().stream().filter(l -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(l.getLogDate());
                            return searchValue != null && parseSearchValue(calendar.get(Calendar.YEAR), searchValue, type, operator);
                        }).collect(Collectors.toList());
                    }
                    else if (fieldToSearch.equals("yearOfLastLog")) {
                        Map<Integer, Log> yearOfLastLog = logs.stream()
                                .filter(l -> l.getLogDate() != null)
                                .collect(Collectors.toMap(Log::getClientId, Function.identity(),
                                        BinaryOperator.maxBy(Comparator.comparing(Log::getLogDate))));
                        filteredData = yearOfLastLog.values().stream().filter(l -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(l.getLogDate());
                            return searchValue != null && parseSearchValue(calendar.get(Calendar.YEAR), searchValue, type, operator);
                        }).collect(Collectors.toList());
                    }
                    else if (fieldToSearch.equals("employeeFlag")) {
                        User user = Database.dao(UserDAO.class).getByUsername((String)searchValue);
                        if (user != null) {
                            filteredData = clientFlags.stream().filter(f -> f.getClientId() == user.getId()).collect(Collectors.toList());
                        }
                    }
                    else if (searchValue != null && !fieldToSearch.equals("totalBalanceShekelsUn")
                    ) {
                        String finalField = fieldToSearch;
                        filteredData = data.stream().filter(d -> {
                            try {
                                return parseSearchValue(FieldUtils.readField(d, finalField, true), searchValue, type, operator);
                            }
                            catch (Exception e) {
                                Logger.error("Error getting field {} from class {}", finalField, d, e);
                                return false;
                            }
                        }).collect(Collectors.toList());
                    }
                }
            }
            if (filteredData != null && !filteredData.isEmpty()) {
                clientIds = filteredData.stream().map(f -> {
                    try {
                        return (int)FieldUtils.readField(f, "clientId", true);
                    }
                    catch (Exception e) {
                        try {
                            int taxYearId = (int)FieldUtils.readField(f, "taxYearId", true);
                            TaxYear taxYear = taxYears.stream().filter(ty -> ty.getId() == taxYearId).findFirst().get();
                            return taxYear.getClientId();
                        }
                        catch (Exception ex) {
                            try {
                                return (int)FieldUtils.readField(f, "id", true);
                            }
                            catch (Exception exception) {
                                Logger.error("Error translating filtered data of type {} to client ids", f.getClass().getSimpleName(), exception);
                                return null;
                            }
                        }
                    }
                }).collect(Collectors.toSet());
            }
            List<Integer> clientIdList = new ArrayList<>(clientIds);

            smartview.setClientIds(clientIdList);
            updatedSmartviews.add(smartview);
        }
        Database.dao(SmartviewDAO.class).bulkUpdateSmartviews(updatedSmartviews);
        Logger.info("UpdateSmartviews Job Complete");
    }

    private List<Object> getTaxYearData(String field, Object searchValue) {
        String[] words = field.split("(?<!^)(?=[A-Z])");

        String filingType = "";
        String searchField = "";
        if (field.contains("StatusDetail")) {
            filingType = words[0];
            searchField = "statusDetail";
        }
        else if (field.contains("Status")) {
            searchField = "status";
            try {
                filingType = words[3];
            }
            catch (Exception e) {
                try {
                    filingType = words[0];
                }
                catch (Exception ex) {
                    Logger.error("Error parsing tax year field {}", field, ex);
                }
            }
        }
        else if (field.equals("extensionForm")) {
            filingType = "ext";
            searchField = "taxForm";
        }
        else if (field.contains("paid") || field.contains("owes")) {
            filingType = words[1];
            searchField = words[0];
        }
        else if (field.equals("year") || field.equals("irsHistory")) {
            return getFilteredData(taxYears, field, searchValue);
        }
        else if (!field.equals("totalRefundCn")) {
            return getFilteredData(filings, field, searchValue);
        }

        String finalFilingType = filingType;
        String finalSearchField = searchField;
        if (!finalFilingType.equals("") && !finalSearchField.equals("")) {
            return filings.stream().filter(f -> {
                try {
                    Object statusDetail = FieldUtils.readField(f, finalSearchField, true);
                    return statusDetail != null && statusDetail.equals(searchValue) && f.getFilingType().equals(finalFilingType);
                }
                catch (IllegalAccessException e) {
                    Logger.error("Error getting field {} for Filing", finalSearchField, e);
                    return false;
                }
            }).collect(Collectors.toList());
        }

        return null;
    }

    private List<Object> getFilteredData(List<?> data, String field, Object searchValue) {
        return data.stream().filter(d -> {
            try {
                Object value = FieldUtils.readField(d, field, true);
                return value != null && value.equals(searchValue);
            }
            catch (Exception e) {
                Logger.error("Error getting field {} for type {}", field, d.getClass().getSimpleName(), e);
                return false;
            }
        }).collect(Collectors.toList());

    }

    private boolean parseSearchValue(Object value, Object searchValue, String type, String operator) {
        if (type != null && value != null && operator != null) {
            searchValue = String.valueOf(searchValue).trim();
            value = String.valueOf(value).trim();
            switch (type) {
                case "boolean":
                    boolean booleanValue = Boolean.parseBoolean((String)value);
                    boolean booleanSearchValue = Boolean.parseBoolean((String)searchValue);
                    if (operator.equals("<")) {
                        return true;
                    }
                    return booleanValue == booleanSearchValue;
                case "int":
                    int intValue = Integer.parseInt((String)value);
                    int intSearchValue = Integer.parseInt((String)searchValue);
                    switch (operator) {
                        case "<":
                            return intValue < intSearchValue;
                        case ">":
                            return intValue > intSearchValue;
                        case "<=":
                            return intValue <= intSearchValue;
                        case ">=":
                            return intValue >= intSearchValue;
                        default:
                            return intValue == intSearchValue;
                    }
                case "Date":
                    Date dateValue = null;
                    Date dateSearchValue = null;
                    try {
                        dateSearchValue = new SimpleDateFormat("MM/dd/yyyy").parse((String)searchValue);
                        dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
                    }
                    catch (ParseException e) {
                        if (((String)searchValue).equalsIgnoreCase("today")) {
                            dateSearchValue = new Date();
                            try {
                                dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        else {
                            try {
                                dateSearchValue = new SimpleDateFormat("yyyy-MM-dd").parse((String)value);
                                dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
                            }
                            catch (Exception exception) {
                                Logger.error("Error parsing date from {}", searchValue, exception);
                                return false;
                            }
                        }
                    }
                    if (dateValue != null && dateSearchValue != null) {
                        switch (operator) {
                            case "<":
                                return dateValue.before(dateSearchValue);
                            case ">":
                                return dateValue.after(dateSearchValue);
                            case "<=":
                                return dateValue.before(dateSearchValue)
                                        || dateValue.equals(dateSearchValue);
                            case ">=":
                                return dateValue.after(dateSearchValue)
                                        || dateValue.equals(dateSearchValue);
                            default:
                                return dateValue.equals(dateSearchValue);
                        }
                    }
                default:
                    return value.equals(searchValue);
            }
        }
        return false;
    }
}
