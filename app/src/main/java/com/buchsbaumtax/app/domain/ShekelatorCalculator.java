package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.core.dao.FilingDAO;
import com.buchsbaumtax.core.dao.IncomeBreakdownDAO;
import com.buchsbaumtax.core.dao.TaxPersonalDAO;
import com.buchsbaumtax.core.model.*;
import com.sifradigital.framework.db.Database;
import org.apache.commons.lang3.time.DateUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShekelatorCalculator {
    public Shekelator getShekelator(TaxYear taxYear) {
        List<Filing> filings = Database.dao(FilingDAO.class).getByTaxYear(taxYear.getId());
        String filingStatus = Objects.requireNonNull(filings.stream().filter(f -> f.getFilingType().equals(Filing.TYPE_FEDERAL)).findFirst().orElse(null)).getStatus();

        Date sixteenYearsAgo = DateUtils.addYears(new Date(), -16);
        List<TaxPersonal> taxPersonals = Database.dao(TaxPersonalDAO.class).getForClient(taxYear.getClientId());
        int childrenUnder16 = (int)taxPersonals.stream().filter(tp -> tp.getDateOfBirth() != null && tp.getDateOfBirth().after(sixteenYearsAgo) && tp.getCategory().equals(TaxPersonal.CATEGORY_DEPENDANT)).count();
        int childrenOver16 = (int)taxPersonals.stream().filter(tp -> tp.getDateOfBirth() != null && tp.getDateOfBirth().before(sixteenYearsAgo) && tp.getCategory().equals(TaxPersonal.CATEGORY_DEPENDANT)).count();

        List<IncomeBreakdown> incomeBreakdowns = Database.dao(IncomeBreakdownDAO.class).getForClient(taxYear.getClientId())
                .stream()
                .filter(ib -> ib.getYears() != null && ib.getYears().equals(taxYear.getYear()))
                .collect(Collectors.toList());
        if (incomeBreakdowns.isEmpty()) {
            throw new WebApplicationException("This client doesn't have any reported income for " + taxYear.getYear(), Response.Status.BAD_REQUEST);
        }
        int childcareCount = (int)incomeBreakdowns.stream()
                .filter(ib -> ib.getDepend() != null).count();
        double childcareExpenses = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup() != null && ib.getTaxGroup().equals(TaxGroup.DEPENDANT_EXPENSES) && ib.getTaxType() != null && ib.getTaxType().equals("CHILD CARE"))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        incomeBreakdowns = incomeBreakdowns.stream().filter(ib -> ib.getTaxGroup() != null && ib.getCategory() != null).collect(Collectors.toList());
        double primaryIncome = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.EARNED_INCOME) && ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double primaryForeignTax = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.TAXES_PAID) && ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double secondaryIncome = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.EARNED_INCOME) && ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double secondaryForeignTax = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.TAXES_PAID) && ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double selfEmploymentIncome = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.SELF_EMPLOYMENT_INCOME) && (ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY) || ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double selfEmploymentForeignTax = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.SELF_EMPLOYMENT_TAXES) && (ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY) || ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double passiveIncome = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.PASSIVE_INCOME) && (ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY) || ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double passiveForeignTax = incomeBreakdowns.stream()
                .filter(ib -> ib.getTaxGroup().equals(TaxGroup.PASSIVE_TAXES) && (ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY) || ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)))
                .mapToDouble(IncomeBreakdown::getAmount).sum();
        double total = primaryIncome + secondaryIncome + selfEmploymentIncome;
        String primaryCurrency = Objects.requireNonNull(incomeBreakdowns.stream()
                .filter(ib -> ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)).findFirst().orElse(null)).getCurrency();
        String secondaryCurrency = Objects.requireNonNull(incomeBreakdowns.stream()
                .filter(ib -> ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)).findFirst().orElse(null)).getCurrency();
        boolean passiveExclusion2555 = incomeBreakdowns.stream()
                .anyMatch(ib -> ib.getTaxGroup().equals(TaxGroup.PASSIVE_TAXES) && (ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY) || ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)) && ib.isExclusion());
        boolean secondaryExclusion2555 = incomeBreakdowns.stream()
                .anyMatch(ib -> ib.getTaxGroup().equals(TaxGroup.EARNED_INCOME) && ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY) && ib.isExclusion());
        boolean primaryExclusion2555 = incomeBreakdowns.stream()
                .anyMatch(ib -> ib.getTaxGroup().equals(TaxGroup.EARNED_INCOME) && ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY) && ib.isExclusion());
        boolean selfEmploymentExclusion2555 = incomeBreakdowns.stream()
                .anyMatch(ib -> ib.getTaxGroup().equals(TaxGroup.SELF_EMPLOYMENT_INCOME) && (ib.getCategory().equals(TaxPersonal.CATEGORY_PRIMARY) || ib.getCategory().equals(TaxPersonal.CATEGORY_SECONDARY)) && ib.isExclusion());

        return new Shekelator(filingStatus, childrenUnder16, childrenOver16, childcareCount, childcareExpenses, primaryIncome, primaryForeignTax, secondaryIncome, secondaryForeignTax, selfEmploymentIncome, selfEmploymentForeignTax, passiveIncome, passiveForeignTax, total, primaryCurrency, secondaryCurrency, passiveExclusion2555, secondaryExclusion2555, primaryExclusion2555, selfEmploymentExclusion2555);
    }
}
