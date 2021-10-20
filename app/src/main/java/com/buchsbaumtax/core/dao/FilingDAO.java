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
    @SqlUpdate("INSERT INTO filings (tax_form, status, status_detail, status_date, memo, include_in_refund, owes, paid, include_fee, owes_fee, paid_fee, file_type, refund, rebate, completed, delivery_contact, second_delivery_contact, date_filed, currency, filing_type, state, tax_year_id, sort_order, amount) VALUES (:taxForm, :status, :statusDetail, :statusDate, :memo, :includeInRefund, :owes, :paid, :includeFee, :owesFee, :paidFee, :fileType, :refund, :rebate, :completed, :deliveryContact, :secondDeliveryContact, :dateFiled, :currency, :filingType, :state, :taxYearId, :sortOrder, :amount)")
    int create(@BindBean Filing filing);

    @RegisterFieldMapper(Filing.class)
    @SqlQuery("SELECT * FROM filings WHERE id = :id")
    Filing get(@Bind("id") int id);

    @RegisterFieldMapper(Filing.class)
    @SqlQuery("SELECT * FROM filings ORDER BY id")
    List<Filing> getAll();

    @SqlUpdate("DELETE FROM filings WHERE id = :id")
    void delete(@Bind("id") int id);

    @RegisterFieldMapper(Filing.class)
    @SqlQuery("SELECT * FROM filings WHERE tax_year_id = :taxYearId")
    List<Filing> getByTaxYear(@Bind("taxYearId") int taxYearId);
}
