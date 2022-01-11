package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.app.dto.SmartviewLineData;
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

public class SmartviewLineUtils {
    BidiMap<String, String> classFieldMap = new DualHashBidiMap<>();

    public SmartviewLineUtils() {
        setClassFieldMap();
    }

    public void setClassFieldMap() {
        classFieldMap.put("FEE::tax_year", "fee::year");
        classFieldMap.put("CONTACT::type", "contact::contact_type");
        classFieldMap.put("LOG::employee_alarm", "log::alarm_user_name");
        classFieldMap.put("LOG::date_of_log", "log::log_date");
        classFieldMap.put("TAX_YEAR::year_name", "tax_year::year");
        classFieldMap.put("TAX_YEAR::tax_form", "filing::tax_form");
        classFieldMap.put("TAX_YEAR::comment", "filing::memo");
        classFieldMap.put("TAX_YEAR::delivery", "filing::delivery_contact");
        classFieldMap.put("TAX_YEAR::tax_state", "filing::state");
        classFieldMap.put("TAX_YEAR::date_filed", "filing::date_filed");
        classFieldMap.put("TAX_YEAR::file_type", "filing::file_type");
        classFieldMap.put("TAX_YEAR::tax_year_status_detail", "filing::status_detail");
        classFieldMap.put("CLIENT_FLAGS::flag_name", "client_flag::flag");
        classFieldMap.put("CLIENT_FLAGS::user_name", "client_flag::user_id");
    }

    public BidiMap<String, String> getClassFieldMap() {
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
            String value = classFieldMap.get(classField);
            splitValues = value.split("::");
        }
        else {
            splitValues = classField.split("::");
        }

        table = splitValues[0].toLowerCase();
        field = splitValues[1];
        String type = getType(table, field);

        if (type != null && type.equals("boolean")) {
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

        for (SmartviewLineData data : smartviewData.getSmartviewLines()) {
            String fieldName = data.getFieldName();
            Map<String, String> values = getLineValues(fieldName, data.getSearchValue());

            SmartviewLine smartviewLine = new SmartviewLine(data.getId(), data.getCreated(), data.getUpdated(), data.getSmartviewId(), data.getGroupNum(), values.get("table"), values.get("field"), values.get("searchValue"), data.getOperator(), values.get("type"));
            smartviewLines.add(smartviewLine);
        }

        return new Smartview(smartviewData.getId(), smartviewData.getUserName(), smartviewData.getUserId(), smartviewData.getName(), smartviewData.getSortNumber(), smartviewData.isArchived(), smartviewData.getClientIds(), smartviewData.getCreated(), smartviewData.getUpdated(), smartviewLines);
    }

    public SmartviewData convertToSmartviewData(Smartview smartview) {
        List<SmartviewLineData> smartviewLineDataList = new ArrayList<>();

        for (SmartviewLine smartviewLine : smartview.getSmartviewLines()) {
            String fieldName = reverseClassFieldMapping(smartviewLine.getTableName(), smartviewLine.getField());

            SmartviewLineData data = new SmartviewLineData(smartviewLine.getId(), smartviewLine.getCreated(), smartviewLine.getUpdated(), smartviewLine.getSmartviewId(), smartviewLine.getGroupNum(), fieldName, smartviewLine.getSearchValue(), smartviewLine.getOperator());
            smartviewLineDataList.add(data);
        }
        return new SmartviewData(smartview.getId(), smartview.getUserName(), smartview.getUserId(), smartview.getName(), smartview.getSortNumber(), smartview.isArchived(), smartview.getClientIds(), smartview.getCreated(), smartview.getUpdated(), smartviewLineDataList);
    }
}
