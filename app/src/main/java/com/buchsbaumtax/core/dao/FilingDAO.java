package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Filing;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface FilingDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO filings (tax_form, status, status_detail, status_date, memo, include_in_refund, owes, paid, include_fee, owes_fee, paid_fee, file_type, refund, rebate, completed, delivery_contact, second_delivery_contact, date_filed, currency, filing_type, state, tax_year_id, sort_order, amount, client_id) VALUES (:taxForm, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :includeFee, :owesFee, :paidFee, :fileType, :refund, :rebate, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :currency, :filingType, :state, :taxYearId, :sortOrder, :amount, :clientId)")
    int create(@BindBean Filing filing);

    @SqlUpdate("UPDATE filings SET tax_form = :taxForm, status = :status, status_detail = :statusDetail, status_date = :statusDate, memo = :memo, include_in_refund = :includeInRefund, owes = :owes, paid = :paid, include_fee = :includeFee, owes_fee = :owesFee, paid_fee = :paidFee, file_type = :fileType, refund = :refund, rebate = :rebate, completed = :completed, delivery_contact = :deliveryContact, second_delivery_contact = :secondDeliveryContact, date_filed = :dateFiled, currency = :currency, filing_type = :filingType, state = :state, tax_year_id = :taxYearId, sort_order = :sortOrder, amount = :amount, client_id = :clientId WHERE id = :id")
    void update(@BindBean Filing filing);

    @RegisterFieldMapper(Filing.class)
    @SqlQuery("SELECT * FROM filings WHERE id = :id")
    Filing get(@Bind("id") int id);

    @RegisterFieldMapper(Filing.class)
    @SqlQuery("SELECT * FROM filings ORDER BY id")
    List<Filing> getAll();

    @SqlUpdate("DELETE FROM filings WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(Filing.class)
    @SqlQuery("SELECT * FROM filings WHERE tax_year_id = :taxYearId ORDER BY date_filed DESC")
    List<Filing> getByTaxYear(@Bind("taxYearId") int taxYearId);
}
