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
    @SqlUpdate("INSERT INTO filings (currency, completed, tax_year_id, date_filed, second_delivery_contact_id, delivery_contact_id, rebate, refund, include_fee, include_in_refund, paid_fee, owes_fee, paid, owes, memo, state, filing_type, status_date, tax_year, file_type, status_detail, status, tax_form) VALUES (:currency, :completed, :taxYearId, :dateFiled, :secondDeliveryContactId, :deliveryContactId,:rebate, :refund, :includeFee, :includeInRefund, :paidFee, :owesFee, :paid, :owes, :memo, :state, :filingType, :statusDate, :taxYear, :fileType, :statusDetail, :status, :taxForm)")
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
