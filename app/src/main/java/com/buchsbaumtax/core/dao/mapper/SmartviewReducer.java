package com.buchsbaumtax.core.dao.mapper;

import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.ArrayList;
import java.util.Map;

public class SmartviewReducer implements LinkedHashMapRowReducer<Integer, Smartview> {
    @Override
    public void accumulate(Map<Integer, Smartview> container, RowView rowView) {
        Smartview smartview = container.computeIfAbsent(rowView.getColumn("s_id", Integer.class),
                id -> rowView.getRow(Smartview.class));

        if (rowView.getColumn("id", Integer.class) != null) {
            if (smartview.getSmartviewLines() == null) {
                smartview.setSmartviewLines(new ArrayList<>());
            }
            smartview.getSmartviewLines().add(rowView.getRow(SmartviewLine.class));
        }
    }
}
