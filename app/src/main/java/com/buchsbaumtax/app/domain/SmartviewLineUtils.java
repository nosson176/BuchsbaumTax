package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.app.dto.SmartviewLineData;
import com.buchsbaumtax.app.dto.SmartviewLineField;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.sifradigital.framework.db.Database;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
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
    List<String> combinedFilings = new ArrayList<>();

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


        combinedFilings.add("date_filed");
        combinedFilings.add("tax_form");
        combinedFilings.add(FIELD_STATUS);
        combinedFilings.add(FIELD_STATUS_DETAIL);
        combinedFilings.add("owes");
        combinedFilings.add("paid");
    }

    public BidiMap<String, SmartviewLineField> getClassFieldMap() {
        return classFieldMap;
    }

    public String reverseClassFieldMapping(SmartviewLine s) {
        String table = s.getTableName();
        String field = s.getField();
        String type = s.getType();
        String searchValue = s.getSearchValue();

        SmartviewLineField smartviewLineField = null;
        if (table.equals(TABLE_FILINGS) && field.equals(FIELD_FILING_TYPE)) {
            SmartviewLine smartviewLine = Database.dao(SmartviewDAO.class).getSmartviewLine(s.getId() + 1);
            if (smartviewLine != null) {
                smartviewLineField = new SmartviewLineField(smartviewLine.getTableName(), smartviewLine.getField(), smartviewLine.getType(), table, field, searchValue);
            }
        }
        else {
            smartviewLineField = new SmartviewLineField(table, field, type);
        }

        if (classFieldMap.containsValue(smartviewLineField)) {
            return classFieldMap.getKey(smartviewLineField);
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

                String searchValue = smartviewLineData.getSearchValue();

                if (field.getType().equals(TYPE_BOOLEAN)) {
                    searchValue = String.valueOf(Boolean.valueOf(searchValue));
                }
                else if (searchValue.equalsIgnoreCase("today")) {
                    searchValue = "now()";
                }

                SmartviewLine line = new SmartviewLine(smartviewLineData, field, searchValue);
                smartviewLines.add(line);
            }
        }
        return new Smartview(smartviewData, smartviewLines);
    }

    public SmartviewData convertToSmartviewData(Smartview smartview) {
        List<SmartviewLineData> smartviewLineDataList = smartview.getSmartviewLines().stream()
                .filter(s -> !(s.getTableName().equals(TABLE_FILINGS) && combinedFilings.contains(s.getField())))
                .map(s -> new SmartviewLineData(setSearchValue(s), reverseClassFieldMapping(s)))
                .collect(Collectors.toList());
        return new SmartviewData(smartview, smartviewLineDataList);
    }

    private SmartviewLine setSearchValue(SmartviewLine smartviewLine) {
        SmartviewLine smartviewLineCopy = SerializationUtils.clone(smartviewLine);

        if (smartviewLineCopy.getSearchValue().equals("now()")) {
            smartviewLineCopy.setSearchValue("today");
        }
        else if (smartviewLineCopy.getType().equals("boolean")) {
            String value = Boolean.parseBoolean(smartviewLineCopy.getSearchValue()) ? "1" : "0";
            smartviewLineCopy.setSearchValue(value);
        }

        if (smartviewLineCopy.getTableName().equals(TABLE_FILINGS) && smartviewLineCopy.getField().equals(FIELD_FILING_TYPE)) {
            SmartviewLine line = Database.dao(SmartviewDAO.class).getSmartviewLine(smartviewLineCopy.getId() + 1);
            if (line != null) {
                smartviewLineCopy.setSearchValue(line.getSearchValue());
            }
        }

        return smartviewLineCopy;
    }
}
