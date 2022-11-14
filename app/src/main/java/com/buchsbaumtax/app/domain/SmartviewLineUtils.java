package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.app.dto.SmartviewLineData;
import com.buchsbaumtax.app.dto.SmartviewLineField;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.google.common.reflect.TypeToken;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.util.ResourceUtils;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SmartviewLineUtils {
    private final String TABLE_FILINGS = "filings";
    private final String FIELD_FILING_TYPE = "filing_type";
    private final String TYPE_BOOLEAN = "boolean";
    BidiMap<String, SmartviewLineField> classFieldMap;
    List<String> combinedFilings;

    public SmartviewLineUtils() {
        classFieldMap = ResourceUtils.loadFromResource("class_field_map.json", new TypeToken<DualHashBidiMap<String, SmartviewLineField>>() {
        }.getType());
        combinedFilings = ResourceUtils.loadFromResource("combined_filings.json", new TypeToken<ArrayList<String>>() {
        }.getType());
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
                    searchValue = searchValue.equals("1") ? "true" : "false";
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
