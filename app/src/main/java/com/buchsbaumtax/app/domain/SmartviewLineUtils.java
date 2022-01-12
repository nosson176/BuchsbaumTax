package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.app.dto.SmartviewLineData;
import com.buchsbaumtax.app.dto.SmartviewLineField;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SmartviewLineUtils {
    private final String TABLE_FILINGS = "filings";
    private final String TABLE_CLIENTS = "clients";
    private final String TABLE_CONTACTS = "contacts";
    private final String TABLE_FEES = "fees";
    private final String TABLE_LOGS = "logs";
    private final String TABLE_INCOME_BREAKDOWNS = "income_breakdowns";
    private final String TABLE_TAX_PERSONALS = "tax_personals";
    private final String TABLE_TAX_YEARS = "tax_years";
    private final String TABLE_CLIENT_FLAGS = "client_flags";

    private final String FIELD_FILING_TYPE = "filing_type";
    private final String FIELD_STATUS = "status";
    private final String FIELD_STATUS_DETAIL = "status_detail";

    private final String STATE = "state";
    private final String FEDERAL = "federal";
    private final String EXT = "ext";
    private final String FBAR = "fbar";

    private final String TYPE_INT = "int";
    private final String TYPE_DOUBLE = "double";
    private final String TYPE_STRING = "String";
    private final String TYPE_DATE = "Date";
    private final String TYPE_BOOLEAN = "boolean";
    BidiMap<String, SmartviewLineField> classFieldMap = new DualHashBidiMap<>();

    public SmartviewLineUtils() {
        setClassFieldMap();
    }

    public void setClassFieldMap() {
        classFieldMap.put("CLIENT::id", new SmartviewLineField(TABLE_CLIENTS, "id", TYPE_INT));
        classFieldMap.put("CLIENT::owes_status", new SmartviewLineField(TABLE_CLIENTS, "owes_status", TYPE_STRING));
        classFieldMap.put("CLIENT::periodical", new SmartviewLineField(TABLE_CLIENTS, "periodical", TYPE_STRING));
        classFieldMap.put("CLIENT::status", new SmartviewLineField(TABLE_CLIENTS, FIELD_STATUS, TYPE_STRING));

        classFieldMap.put("CONTACT::city", new SmartviewLineField(TABLE_CONTACTS, "secondary_detail", TYPE_STRING));
        classFieldMap.put("CONTACT::state", new SmartviewLineField(TABLE_CONTACTS, STATE, TYPE_STRING));
        classFieldMap.put("CONTACT::type", new SmartviewLineField(TABLE_CONTACTS, "contact_type", TYPE_STRING));

        classFieldMap.put("FEE::fee_type", new SmartviewLineField(TABLE_FEES, "fee_type", TYPE_STRING));
        classFieldMap.put("FEE::status", new SmartviewLineField(TABLE_FEES, FIELD_STATUS, TYPE_STRING));
        classFieldMap.put("FEE::status_detail", new SmartviewLineField(TABLE_FEES, FIELD_STATUS_DETAIL, TYPE_STRING));
        classFieldMap.put("FEE::tax_year", new SmartviewLineField(TABLE_FEES, "year", TYPE_STRING));

        classFieldMap.put("INCOME_BREAKDOWN::currency", new SmartviewLineField(TABLE_INCOME_BREAKDOWNS, "currency", TYPE_STRING));

        classFieldMap.put("LOG::alarm_complete", new SmartviewLineField(TABLE_LOGS, "alarm_complete", TYPE_BOOLEAN));
        classFieldMap.put("LOG::alarm_date", new SmartviewLineField(TABLE_LOGS, "alarm_date", TYPE_DATE));
        classFieldMap.put("LOG::employee_alarm", new SmartviewLineField(TABLE_LOGS, "alarm_user_name", TYPE_STRING));
        classFieldMap.put("LOG::date_of_log", new SmartviewLineField(TABLE_LOGS, "log_date", TYPE_DATE));

        classFieldMap.put("TAX_PERSONAL::category", new SmartviewLineField(TABLE_TAX_PERSONALS, "category", TYPE_STRING));
        classFieldMap.put("TAX_PERSONAL::ssn", new SmartviewLineField(TABLE_TAX_PERSONALS, "ssn", TYPE_STRING));

        classFieldMap.put("TAX_YEAR::comment", new SmartviewLineField(TABLE_FILINGS, "memo", TYPE_STRING));
        classFieldMap.put("TAX_YEAR::date_filed", new SmartviewLineField(TABLE_FILINGS, "date_filed", TYPE_DATE, TABLE_FILINGS, FIELD_FILING_TYPE, FEDERAL));
        classFieldMap.put("TAX_YEAR::extension_form", new SmartviewLineField(TABLE_FILINGS, "tax_form", TYPE_STRING, TABLE_FILINGS, FIELD_FILING_TYPE, EXT));
        classFieldMap.put("TAX_YEAR::extension_status", new SmartviewLineField(TABLE_FILINGS, FIELD_STATUS, TYPE_STRING, TABLE_FILINGS, FIELD_FILING_TYPE, EXT));
        classFieldMap.put("TAX_YEAR::FBAR_status", new SmartviewLineField(TABLE_FILINGS, FIELD_STATUS, TYPE_STRING, TABLE_FILINGS, FIELD_FILING_TYPE, FBAR));
        classFieldMap.put("TAX_YEAR::FBAR_status_detail", new SmartviewLineField(TABLE_FILINGS, FIELD_STATUS_DETAIL, TYPE_STRING, TABLE_FILINGS, FIELD_FILING_TYPE, FBAR));
        classFieldMap.put("TAX_YEAR::file_type", new SmartviewLineField(TABLE_FILINGS, "file_type", TYPE_STRING));
        classFieldMap.put("TAX_YEAR::IRS_HISTORY", new SmartviewLineField(TABLE_TAX_YEARS, "irs_history", TYPE_BOOLEAN));
        classFieldMap.put("TAX_YEAR::owes_federal", new SmartviewLineField(TABLE_FILINGS, "owes", TYPE_DOUBLE, TABLE_FILINGS, FIELD_FILING_TYPE, FEDERAL));
        classFieldMap.put("TAX_YEAR::paid_federal", new SmartviewLineField(TABLE_FILINGS, "paid", TYPE_DOUBLE, TABLE_FILINGS, FIELD_FILING_TYPE, FEDERAL));
        classFieldMap.put("TAX_YEAR::state_status_detail", new SmartviewLineField(TABLE_FILINGS, FIELD_STATUS_DETAIL, TYPE_STRING, TABLE_FILINGS, FIELD_FILING_TYPE, STATE));
        classFieldMap.put("TAX_YEAR::tax_form", new SmartviewLineField(TABLE_FILINGS, "tax_form", TYPE_STRING));
        classFieldMap.put("TAX_YEAR::tax_year_status_detail", new SmartviewLineField(TABLE_FILINGS, FIELD_STATUS_DETAIL, TYPE_STRING));
        classFieldMap.put("TAX_YEAR::tax_year_status_federal", new SmartviewLineField(TABLE_FILINGS, FIELD_STATUS, TYPE_STRING, TABLE_FILINGS, FIELD_FILING_TYPE, FEDERAL));
        classFieldMap.put("TAX_YEAR::tax_year_status_state", new SmartviewLineField(TABLE_FILINGS, FIELD_STATUS, TYPE_STRING, TABLE_FILINGS, FIELD_FILING_TYPE, STATE));
        classFieldMap.put("TAX_YEAR::year_name", new SmartviewLineField(TABLE_TAX_YEARS, "year", TYPE_STRING));
        classFieldMap.put("TAX_YEAR::delivery", new SmartviewLineField(TABLE_FILINGS, "delivery_contact", TYPE_STRING));
        classFieldMap.put("TAX_YEAR::tax_state", new SmartviewLineField(TABLE_FILINGS, STATE, TYPE_STRING));

        classFieldMap.put("CLIENT_FLAGS::flag_name", new SmartviewLineField(TABLE_CLIENT_FLAGS, "flag", TYPE_INT));
        classFieldMap.put("CLIENT_FLAGS::user_name", new SmartviewLineField(TABLE_CLIENT_FLAGS, "user_id", TYPE_INT));
    }

    public BidiMap<String, SmartviewLineField> getClassFieldMap() {
        return classFieldMap;
    }

    public String getType(String table, String field) {
        String tableName = "com.buchsbaumtax.core.model." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table);
        String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, field);
        try {
            Class<?> c = Class.forName(tableName);
            for (Field f : c.getDeclaredFields()) {
                if (fieldName.equals(f.getName())) {
                    return f.getType().getSimpleName();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getLineValues(String classField, String searchValue) {
        Map<String, String> values = new HashMap<>();
        String[] splitValues;
        String table;
        String field;
        if (classFieldMap.containsKey(classField)) {
            String value = "";//classFieldMap.get(classField);
            splitValues = value.split("::");
        }
        else {
            splitValues = classField.split("::");
        }

        table = splitValues[0].toLowerCase();
        field = splitValues[1];
        String type = getType(table, field);

        if (type != null && type.equals(TYPE_BOOLEAN)) {
            searchValue = String.valueOf(Boolean.valueOf(searchValue));
        }
        else if (searchValue != null && searchValue.equalsIgnoreCase("today")) {
            searchValue = "now()";
        }

        values.put("table", table + "s");
        values.put("field", field);
        values.put("type", type);
        values.put("searchValue", searchValue);

        return values;
    }

    public String reverseClassFieldMapping(String table, String field) {
        table = StringUtils.chop(table);
        String classField = table + "::" + field;

        if (classFieldMap.containsValue(classField)) {
            return classFieldMap.getKey(classField);
        }
        else {
            return table.toUpperCase() + "::" + field;
        }
    }

    public Smartview convertToSmartview(SmartviewData smartviewData) {
        List<SmartviewLine> smartviewLines = new ArrayList<>();

        for (SmartviewLineData smartviewLineData : smartviewData.getSmartviewLines()) {
            String fieldName = smartviewLineData.getFieldName();

            SmartviewLineField field = classFieldMap.get(fieldName);

            if (field != null) {
                if (field.getTableName2() != null) {
                    SmartviewLine line = new SmartviewLine(smartviewLineData, new SmartviewLineField(field.getTableName2(), field.getFieldName2(), "String"), field.getSearchValue());
                    smartviewLines.add(line);
                }

                SmartviewLine line = new SmartviewLine(smartviewLineData, field, smartviewLineData.getSearchValue());
                smartviewLines.add(line);
            }
        }
        return new Smartview(smartviewData, smartviewLines);
    }

    public Map<String, String> getTaxYearValues(String fieldName) {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("tax_year_status_state", "state_status");
        mappings.put("tax_year_status_federal", "federal_status");
        mappings.put("extension_status", "ext_status");
        mappings.put("extension_form", "ext_form");

        String[] result = fieldName.split("::");

        String field = result[1];
        if (mappings.containsKey(field)) {
            field = mappings.get(field);
        }

        String[] searchParts = field.split("_");

        Map<String, String> values = new HashMap<>();
        if (fieldName.contains(FIELD_STATUS)) {
            values.put("searchValue", searchParts[0]);

            if (fieldName.contains("detail")) {
                values.put("fieldName", "filing::status_detail");
            }
            else {
                values.put("fieldName", "filing::status");
            }
        }
        else if (fieldName.contains("form")) {
            values.put("searchValue", searchParts[0]);
            values.put("fieldName", "filing::tax_form");
        }
        else if (fieldName.contains("owes") || field.contains("paid")) {
            values.put("searchValue", searchParts[1]);
            values.put("fieldName", "filing::" + searchParts[0]);
        }
        return values;
    }

    public SmartviewData convertToSmartviewData(Smartview smartview) {
        List<SmartviewLineData> smartviewLineDataList = smartview.getSmartviewLines().stream().map(s -> new SmartviewLineData(s, reverseClassFieldMapping(s.getTableName(), s.getField()))).collect(Collectors.toList());
        return new SmartviewData(smartview, smartviewLineDataList);
    }
}
