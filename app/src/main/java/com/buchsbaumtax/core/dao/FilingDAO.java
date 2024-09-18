package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.core.model.Filing;
import com.buchsbaumtax.core.model.Status;
import com.buchsbaumtax.core.util.JsonArgumentFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Dao
@RegisterRowMapper(FilingDAO.FilingMapper.class)
@RegisterArgumentFactory(JsonArgumentFactory.class)
public interface FilingDAO {
    Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO filings (tax_form, status, status_detail, status_date, memo, include_in_refund, owes, paid, include_fee, owes_fee, paid_fee, file_type, refund, rebate, completed, delivery_contact, second_delivery_contact, date_filed, currency, filing_type, state, tax_year_id, sort_order, amount, client_id) " +
            "VALUES (:taxForm, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :includeFee, :owesFee, :paidFee, :fileType, :refund, :rebate, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :currency, :filingType, :state, :taxYearId, :sortOrder, :amount, :clientId)")
    int create(@BindBean Filing filing);

    @SqlUpdate("UPDATE filings SET tax_form = :taxForm, status = :status, status_detail = :statusDetail, status_date = :statusDate, memo = :memo, include_in_refund = :includeInRefund, owes = :owes, paid = :paid, include_fee = :includeFee, owes_fee = :owesFee, paid_fee = :paidFee, file_type = :fileType, refund = :refund, rebate = :rebate, completed = :completed, delivery_contact = :deliveryContact, second_delivery_contact = :secondDeliveryContact, date_filed = :dateFiled, currency = :currency, filing_type = :filingType, state = :state, tax_year_id = :taxYearId, sort_order = :sortOrder, amount = :amount WHERE id = :id")
    void update(@BindBean Filing filing);

    @SqlQuery("SELECT * FROM filings WHERE id = :id")
    Filing get(@Bind("id") int id);

    @SqlQuery("SELECT * FROM filings ORDER BY id")
    List<Filing> getAll();

    @SqlUpdate("DELETE FROM filings WHERE id = :id")
    void delete(@Bind("id") int id);

    @SqlQuery("SELECT * FROM filings WHERE tax_year_id = :taxYearId ORDER BY sort_order, filing_type")
    List<Filing> getByTaxYear(@Bind("taxYearId") int taxYearId);

    @SqlBatch("UPDATE filings SET tax_form = :taxForm, status = :status, status_detail = :statusDetail, status_date = :statusDate, memo = :memo, include_in_refund = :includeInRefund, owes = :owes, paid = :paid, include_fee = :includeFee, owes_fee = :owesFee, paid_fee = :paidFee, file_type = :fileType, refund = :refund, rebate = :rebate, completed = :completed, delivery_contact = :deliveryContact, second_delivery_contact = :secondDeliveryContact, date_filed = :dateFiled, currency = :currency, filing_type = :filingType, state = :state, tax_year_id = :taxYearId, sort_order = :sortOrder, amount = :amount WHERE id = :id")
    void update(@BindBean List<Filing> filings);

    @SqlQuery("SELECT * FROM filings WHERE client_id = :clientId")
    List<Filing> getByClient(@Bind("clientId") int clientId);

    class FilingMapper implements RowMapper<Filing> {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Filing map(ResultSet rs, StatementContext ctx) throws SQLException {
            Filing filing = new Filing();

            // Map standard fields
            filing.setId(rs.getInt("id"));
            filing.setTaxForm(stripQuotes(rs.getString("tax_form")));
            filing.setStatusDate(getLongFromBigDecimal(rs.getBigDecimal("status_date")));
            filing.setMemo(stripQuotes(rs.getString("memo")));
            filing.setIncludeInRefund(rs.getBoolean("include_in_refund"));
            filing.setOwes(getDoubleFromBigDecimal(rs.getBigDecimal("owes")));
            filing.setPaid(getDoubleFromBigDecimal(rs.getBigDecimal("paid")));
            filing.setIncludeFee(rs.getBoolean("include_fee"));
            filing.setOwesFee(getDoubleFromBigDecimal(rs.getBigDecimal("owes_fee")));
            filing.setPaidFee(getDoubleFromBigDecimal(rs.getBigDecimal("paid_fee")));
            filing.setRefund(getDoubleFromBigDecimal(rs.getBigDecimal("refund")));
            filing.setRebate(getDoubleFromBigDecimal(rs.getBigDecimal("rebate")));
            filing.setAmount(getDoubleFromBigDecimal(rs.getBigDecimal("amount")));
            filing.setCompleted(rs.getBoolean("completed"));
            filing.setFileType(stripQuotes(rs.getString("file_type")));
            filing.setDeliveryContact(rs.getString("delivery_contact"));
            filing.setSecondDeliveryContact(rs.getString("second_delivery_contact"));
            filing.setDateFiled(getLongFromBigDecimal(rs.getBigDecimal("date_filed")));
            filing.setCurrency(stripQuotes(rs.getString("currency")));
            filing.setFilingType(stripQuotes(rs.getString("filing_type")));
            filing.setState(rs.getString("state"));
            filing.setTaxYearId(rs.getInt("tax_year_id"));
            filing.setSortOrder(rs.getInt("sort_order"));
            filing.setClientId(rs.getInt("client_id"));

            // Handle JSON fields
          filing.setStatus(parseJson(rs.getString("status"), Status.class, "status"));
            filing.setStatusDetail(parseJson(rs.getString("status_detail"), Status.class, "status_detail"));

            logger.info("bigDecimal : {}", filing);
            return filing;
        }

        // Utility to convert BigDecimal to long
        private Long getLongFromBigDecimal(BigDecimal value) {
            return value != null ? value.longValue() : null;
        }

        private String stripQuotes(String value) {
            logger.info("value : {}", value);
            if (value != null && value.length() > 1 && value.startsWith("\"") && value.endsWith("\"")) {
                return value.substring(1, value.length() - 1);
            }
            return value;
        }

        // Utility to convert BigDecimal to double
        public Double getDoubleFromBigDecimal(BigDecimal bigDecimal) {
            logger.info("bigDecimal : {}", bigDecimal);
            if (bigDecimal == null) {
                logger.info("bigDecimal inside!!!");
                return 0.0;  // Or any appropriate default value
            }
            return bigDecimal.doubleValue();
        }

        // Generic method to parse JSON
        private <T> T parseJson(String json, Class<T> valueType, String fieldName) {
            if (json == null || json.isEmpty()) {
                logger.info("{} JSON is null or empty", fieldName);
                return null;
            }
            try {
                logger.info("Parsing JSON for field '{}'. JSON: {}, ValueType: {}", fieldName, json, valueType);

                // Parse the JSON and store the result in a variable
                T result = objectMapper.readValue(json, valueType);

                // Log the result
                logger.info("Parsed result for field '{}': {}", fieldName, result);

                // Return the result
                return result;
            } catch (Exception e) {
                logger.error("Error parsing JSON for field '{}': {}", fieldName, json, e);
                return null;
            }
        }

    }
}
