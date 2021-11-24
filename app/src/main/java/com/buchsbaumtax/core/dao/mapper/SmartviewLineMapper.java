package com.buchsbaumtax.core.dao.mapper;

import com.buchsbaumtax.core.model.SmartviewLine;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SmartviewLineMapper implements RowMapper<SmartviewLine> {
    @Override
    public SmartviewLine map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new SmartviewLine(rs.getInt("id"), rs.getDate("created"), rs.getDate("updated"), rs.getLong("smartview_id"), rs.getLong("query"), rs.getString("class_to_join"), rs.getString("field_to_search"), rs.getString("search_value"), rs.getString("operator"), rs.getString("type"));
    }
}
